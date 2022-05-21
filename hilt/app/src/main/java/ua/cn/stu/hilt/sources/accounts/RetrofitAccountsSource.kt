package ua.cn.stu.hilt.sources.accounts

import kotlinx.coroutines.delay
import ua.cn.stu.hilt.app.model.accounts.AccountsSource
import ua.cn.stu.hilt.app.model.accounts.entities.Account
import ua.cn.stu.hilt.app.model.accounts.entities.SignUpData
import ua.cn.stu.hilt.sources.accounts.entities.SignInRequestEntity
import ua.cn.stu.hilt.sources.accounts.entities.SignUpRequestEntity
import ua.cn.stu.hilt.sources.accounts.entities.UpdateUsernameRequestEntity
import ua.cn.stu.hilt.sources.base.BaseRetrofitSource
import ua.cn.stu.hilt.sources.base.RetrofitConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitAccountsSource @Inject constructor(
    config: RetrofitConfig
) : BaseRetrofitSource(config), AccountsSource {

    private val accountsApi = retrofit.create(AccountsApi::class.java)

    override suspend fun signIn(
        email: String,
        password: String
    ): String = wrapRetrofitExceptions {
        delay(1000)
        val signInRequestEntity = SignInRequestEntity(email, password)
        accountsApi.signIn(signInRequestEntity).token
    }

    override suspend fun signUp(
        signUpData: SignUpData
    ) = wrapRetrofitExceptions {
        delay(1000)
        val signUpRequestEntity = SignUpRequestEntity(
            signUpData.email,
            signUpData.username,
            signUpData.password
        )
        accountsApi.signUp(signUpRequestEntity)
    }

    override suspend fun getAccount(): Account = wrapRetrofitExceptions {
        delay(1000)
        accountsApi.getAccount().toAccount()
    }

    override suspend fun setUsername(
        username: String
    ) = wrapRetrofitExceptions {
        delay(1000)
        val updateUsernameRequestEntity = UpdateUsernameRequestEntity(username)
        accountsApi.setUsername(updateUsernameRequestEntity)
    }

}