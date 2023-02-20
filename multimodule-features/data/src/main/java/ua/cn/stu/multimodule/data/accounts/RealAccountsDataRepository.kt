package ua.cn.stu.multimodule.data.accounts

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.core.AuthException
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.flow.LazyFlowSubjectFactory
import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.data.accounts.entities.AccountDataEntity
import ua.cn.stu.multimodule.data.accounts.entities.SignUpDataEntity
import ua.cn.stu.multimodule.data.accounts.sources.AccountsDataSource
import ua.cn.stu.multimodule.data.settings.SettingsDataSource
import javax.inject.Inject

class RealAccountsDataRepository @Inject constructor(
    private val accountsDataSource: AccountsDataSource,
    private val settingsDataSource: SettingsDataSource,
    coroutineScope: CoroutineScope,
    lazyFlowSubjectFactory: LazyFlowSubjectFactory,
) : AccountsDataRepository {

    private val accountLazyFlowSubject = lazyFlowSubjectFactory.create {
        accountsDataSource.getAccount()
    }

    init {
        coroutineScope.launch {
            settingsDataSource.listenToken().collect {
                if (it != null) {
                    accountLazyFlowSubject.newAsyncLoad(silently = true)
                } else {
                    accountLazyFlowSubject.updateWith(Container.Error(AuthException()))
                }
            }
        }
    }

    override fun getAccount(): Flow<Container<AccountDataEntity>> {
        return accountLazyFlowSubject.listen()
    }

    override suspend fun setAccountUsername(username: String) {
        val newAccount = accountsDataSource.setAccountUsername(username)
        accountLazyFlowSubject.updateWith(Container.Success(newAccount))
    }

    override suspend fun signIn(email: String, password: String): String {
        return accountsDataSource.signIn(email, password)
    }

    override suspend fun signUp(signUpData: SignUpDataEntity) {
        accountsDataSource.signUp(signUpData)
    }

    override fun reload() {
        accountLazyFlowSubject.newAsyncLoad()
    }

}