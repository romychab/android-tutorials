package ua.cn.stu.tests.domain.boxes

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import ua.cn.stu.tests.domain.*
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.utils.async.LazyFlowSubject
import ua.cn.stu.tests.domain.accounts.entities.Account
import ua.cn.stu.tests.domain.boxes.entities.Box
import ua.cn.stu.tests.domain.boxes.entities.BoxAndSettings
import ua.cn.stu.tests.domain.boxes.entities.BoxesFilter
import ua.cn.stu.tests.domain.settings.AppSettings
import ua.cn.stu.tests.utils.async.LazyFlowFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxesRepository @Inject constructor(
    private val accountsRepository: AccountsRepository,
    private val boxesSource: BoxesSource,
    lazyFlowFactory: LazyFlowFactory
) {

    private var accountResult: Result<Account> = Empty()

    private val boxesLazyFlowSubject: LazyFlowSubject<BoxesFilter, List<BoxAndSettings>> =
        lazyFlowFactory.createLazyFlowSubject { filter ->
            wrapBackendExceptions { boxesSource.getBoxes(filter) }
        }

    /**
     * Get the list of boxes.
     * @return infinite flow, always success; errors are wrapped to [Result]
     */
    fun getBoxesAndSettings(filter: BoxesFilter): Flow<Result<List<BoxAndSettings>>> {
        return accountsRepository.getAccount()
            .onEach {
                accountResult = it
            }
            .flatMapLatest { result ->
                if (result is Success) {
                    // has new account data -> reload boxes
                    boxesLazyFlowSubject.listen(filter)
                } else {
                    flowOf(result.map())
                }
            }
    }

    /**
     * Reload the list of boxes.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    fun reload(filter: BoxesFilter) {
        if (accountResult is Error) {
            // failed to load account -> try it again;
            // after loading account, boxes will be loaded automatically
            accountsRepository.reloadAccount()
        } else {
            boxesLazyFlowSubject.reloadArgument(filter)
        }
    }

    /**
     * Mark the specified box as active. Only active boxes are displayed in dashboard screen.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun activateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, true)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

    /**
     * Mark the specified box as inactive. Inactive boxes are not displayed in dashboard screen.
     * @throws AuthException
     * @throws BackendException
     * @throws ConnectionException
     */
    suspend fun deactivateBox(box: Box) = wrapBackendExceptions {
        boxesSource.setIsActive(box.id, false)
        boxesLazyFlowSubject.reloadAll(silentMode = true)
    }

}