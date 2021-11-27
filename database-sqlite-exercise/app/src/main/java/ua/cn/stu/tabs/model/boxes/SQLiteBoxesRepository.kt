package ua.cn.stu.tabs.model.boxes

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import ua.cn.stu.tabs.model.AuthException
import ua.cn.stu.tabs.model.accounts.AccountsRepository
import ua.cn.stu.tabs.model.boxes.entities.Box
import ua.cn.stu.tabs.model.sqlite.wrapSQLiteException

class SQLiteBoxesRepository(
    private val db: SQLiteDatabase,
    private val accountsRepository: AccountsRepository,
    private val ioDispatcher: CoroutineDispatcher
) : BoxesRepository {

    private val reconstructFlow = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    override suspend fun getBoxes(onlyActive: Boolean): Flow<List<Box>> {
        return combine(accountsRepository.getAccount(), reconstructFlow) { account, _ ->
                queryBoxes(onlyActive, account?.id)
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun activateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, true)
    }

    override suspend fun deactivateBox(box: Box) = wrapSQLiteException(ioDispatcher) {
        setActiveFlagForBox(box, false)
    }

    private suspend fun setActiveFlagForBox(box: Box, isActive: Boolean) {
        val account = accountsRepository.getAccount().first() ?: throw AuthException()
        saveActiveFlag(account.id, box.id, isActive)
        reconstructFlow.tryEmit(Unit)
    }

    private fun queryBoxes(onlyActive: Boolean, accountId: Long?): List<Box> {
        if (accountId == null) return emptyList()

        val cursor = queryBoxes(onlyActive, accountId)
        return cursor.use {
            val list = mutableListOf<Box>()
            while (cursor.moveToNext()) {
                list.add(parseBox(cursor))
            }
            return@use list
        }
    }

    private fun parseBox(cursor: Cursor): Box {
        TODO("#7 \n" +
                "1) Get id, color name and color value from the cursor here \n" +
                "2) Create a Box object")
    }

    private fun saveActiveFlag(accountId: Long, boxId: Long, isActive: Boolean) {
        TODO("#8 \n" +
                "Insert or update isActive flag in the accounts_boxes_settings table here \n" +

                "Tip: use SQLiteDatabase.insertWithOnConflict method")
    }

    private fun queryBoxes(onlyActive: Boolean, accountId: Long): Cursor {
        if (onlyActive) {
            TODO("#10 \n" +
                    "Return a cursor which selects only those rows from boxes table \n" +
                    "which doesn't have setting in the accounts_boxes_settings table or which \n" +
                    "have such setting and it's = 1 (true) \n" +

                    "Tip: use rawQuery and LEFT JOIN to combine data from 2 tables")
        } else {
            TODO("#9 \n" +
                    "Just return a cursor with selects all rows from boxes table")
        }
    }
}