package ua.cn.stu.http.sources.accounts

import kotlinx.coroutines.delay
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.model.accounts.entities.SignUpData
import ua.cn.stu.http.sources.base.BaseRetrofitSource
import ua.cn.stu.http.sources.base.RetrofitConfig

// todo #7: implement AccountSource methods:
//          - signIn -> should call 'POST /sign-in' and return a token
//          - signUp -> should call ' POST /sign-up'
//          - getAccount -> should call 'GET /me' and return account data
//          - setUsername -> should call 'PUT /me'
class RetrofitAccountsSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), AccountsSource {

    override suspend fun signIn(
        email: String,
        password: String
    ): String {
        delay(1000)
        TODO()
    }

    override suspend fun signUp(
        signUpData: SignUpData
    ) {
        delay(1000)
        TODO()
    }

    override suspend fun getAccount(): Account {
        delay(1000)
        TODO()
    }

    override suspend fun setUsername(
        username: String
    ) {
        delay(1000)
        TODO()
    }

}