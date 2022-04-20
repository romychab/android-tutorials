package ua.cn.stu.http.sources.accounts

import kotlinx.coroutines.delay
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.model.accounts.entities.SignUpData
import ua.cn.stu.http.sources.base.BaseOkHttpSource
import ua.cn.stu.http.sources.base.OkHttpConfig

// todo #6: implement methods:
//          - signIn() -> for exchanging email+password to token
//          - signUp() -> for creating a new account
//          - getAccount() -> for fetching account info
//          - setUsername() -> for editing username
class OkHttpAccountsSource(
    config: OkHttpConfig
) : BaseOkHttpSource(config), AccountsSource {

    override suspend fun signIn(email: String, password: String): String {
        delay(1000)
        // Call "POST /sign-in" endpoint and return token.
        // Use SignInRequestEntity and SignInResponseEntity.
        TODO()
    }

    override suspend fun signUp(signUpData: SignUpData) {
        delay(1000)
        // Call "POST /sign-up" endpoint.
        // Use SignUpRequestEntity
        TODO()
    }

    override suspend fun getAccount(): Account {
        delay(1000)
        // Call "GET /me" endpoint.
        // Use GetAccountResponseEntity.
        TODO()
    }

    override suspend fun setUsername(username: String) {
        delay(1000)
        // Call "PUT /me" endpoint.
        // Use UpdateUsernameRequestEntity.
        TODO()
    }

}