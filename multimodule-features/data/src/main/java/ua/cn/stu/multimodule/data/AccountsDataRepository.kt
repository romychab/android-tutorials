package ua.cn.stu.multimodule.data

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.accounts.entities.AccountDataEntity
import ua.cn.stu.multimodule.data.accounts.entities.SignUpDataEntity
import ua.cn.stu.multimodule.data.accounts.exceptions.AccountAlreadyExistsDataException

interface AccountsDataRepository {

    /**
     * Listen for the current account.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getAccount(): Flow<Container<AccountDataEntity>>

    /**
     * Update the username of the current logged-in user.
     */
    suspend fun setAccountUsername(username: String)

    /**
     * Exchange email-password to auth token.
     */
    suspend fun signIn(email: String, password: String): String

    /**
     * Create a new account.
     * @throws AccountAlreadyExistsDataException
     */
    suspend fun signUp(signUpData: SignUpDataEntity)

    /**
     * Reload the flow returned by [getAccount]
     */
    fun reload()

}