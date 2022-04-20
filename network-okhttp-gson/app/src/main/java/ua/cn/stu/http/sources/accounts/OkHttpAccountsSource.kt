package ua.cn.stu.http.sources.accounts

import kotlinx.coroutines.delay
import okhttp3.Request
import ua.cn.stu.http.app.model.accounts.AccountsSource
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.model.accounts.entities.SignUpData
import ua.cn.stu.http.sources.accounts.entities.*
import ua.cn.stu.http.sources.base.BaseOkHttpSource
import ua.cn.stu.http.sources.base.OkHttpConfig

class OkHttpAccountsSource(
    config: OkHttpConfig
) : BaseOkHttpSource(config), AccountsSource {

    override suspend fun signIn(email: String, password: String): String {
        delay(1000)
        val signInRequestEntity = SignInRequestEntity(email, password)
        val request = Request.Builder()
            .post(signInRequestEntity.toJsonRequestBody())
            .endpoint("/sign-in")
            .build()
        val response = client.newCall(request).suspendEnqueue()
        return response.parseJsonResponse<SignInResponseEntity>().token
    }

    override suspend fun signUp(signUpData: SignUpData) {
        delay(1000)
        val signUpRequestEntity = SignUpRequestEntity(
            signUpData.email,
            signUpData.username,
            signUpData.password
        )
        val request = Request.Builder()
            .post(signUpRequestEntity.toJsonRequestBody())
            .endpoint("/sign-up")
            .build()
        client.newCall(request).suspendEnqueue()
    }

    override suspend fun getAccount(): Account {
        delay(1000)
        val request = Request.Builder()
            .get()
            .endpoint("/me")
            .build()
        val response = client.newCall(request).suspendEnqueue()
        val accountEntity = response.parseJsonResponse<GetAccountResponseEntity>()
        return accountEntity.toAccount()
    }

    override suspend fun setUsername(username: String) {
        delay(1000)
        val updateUsernameRequestEntity = UpdateUsernameRequestEntity(username)
        val request = Request.Builder()
            .put(updateUsernameRequestEntity.toJsonRequestBody())
            .endpoint("/me")
            .build()
        val call = client.newCall(request)
        call.suspendEnqueue()
    }

}