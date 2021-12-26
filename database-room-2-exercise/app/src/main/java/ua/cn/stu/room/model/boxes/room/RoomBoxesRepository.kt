package ua.cn.stu.room.model.boxes.room

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import ua.cn.stu.room.model.AuthException
import ua.cn.stu.room.model.accounts.AccountsRepository
import ua.cn.stu.room.model.boxes.BoxesRepository
import ua.cn.stu.room.model.boxes.entities.Box
import ua.cn.stu.room.model.boxes.entities.BoxAndSettings
import ua.cn.stu.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ua.cn.stu.room.model.room.wrapSQLiteException

class RoomBoxesRepository(
    private val accountsRepository: AccountsRepository,
    private val boxesDao: BoxesDao,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    override suspend fun getBoxesAndSettings(onlyActive: Boolean): Flow<List<BoxAndSettings>> {
        return accountsRepository.getAccount()
            .flatMapLatest { account ->
                if (account == null) return@flatMapLatest flowOf(emptyList())
                queryBoxesAndSettings(account.id)
            }
            .mapLatest { boxAndSettings ->
                if (onlyActive) {
                    boxAndSettings.filter { it.isActive }
                } else {
                    boxAndSettings
                }
            }
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private fun queryBoxesAndSettings(accountId: Long): Flow<List<BoxAndSettings>> {
        return boxesDao.getBoxesAndSettings(accountId)
            .map { entities ->
                entities.map {
                    // todo #7: use embedded entities instead of keys and values;
                    //          now launch the project and check how it works.
                    val boxEntity = it.key
                    val settingsEntity = it.value
                    // todo #3: use embedded entity instead of isActive property
                    BoxAndSettings(boxEntity.toBox(), settingsEntity == null || settingsEntity.isActive)
                }
            }
    }

    // todo #12: Rewrite queryBoxesAndSettings() method above ^ for usage with database view.
    //           Uninstall the app from the device, install it again, launch and check all things work correctly.

    // todo #16: Rewrite queryBoxesAndSettings() method above ^ again for usage with SettingWithEntitiesTuple.
    //           Uninstall the app from the device, install it again, launch and check all things work correctly.

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        val account = accountsRepository.getAccount().first() ?: throw AuthException()
        boxesDao.setActiveFlagForBox(
            AccountBoxSettingDbEntity(
                accountId = account.id,
                boxId = box.id,
                // todo #4: use embedded entity instead of isActive property
                isActive = isActive
            )
        )
    }
}