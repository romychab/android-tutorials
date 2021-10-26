package ua.cn.stu.navcomponent.tabs.model.accounts

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ua.cn.stu.navcomponent.tabs.model.*
import ua.cn.stu.navcomponent.tabs.model.accounts.entities.Account
import ua.cn.stu.navcomponent.tabs.model.accounts.entities.SignUpData

/**
 * Simple implementation of [AccountsRepository] which holds accounts data in the app memory.
 */
class InMemoryAccountsRepository : AccountsRepository {

    private var currentAccountFlow = MutableStateFlow<Account?>(null)

    private val accounts = mutableListOf(
        AccountRecord(
            account = Account(
                username = "admin",
                email = "admin@google.com"
            ),
            password = "123"
        )
    )

    init {
        currentAccountFlow.value = accounts[0].account
    }

    override suspend fun isSignedIn(): Boolean {
        delay(2000)
        return currentAccountFlow.value != null
    }

    override suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)

        delay(1000)
        val record = getRecordByEmail(email)
        if (record != null && record.password == password) {
            currentAccountFlow.value = record.account
        } else {
            throw AuthException()
        }
    }

    override suspend fun signUp(signUpData: SignUpData) {
        signUpData.validate()

        delay(1000)
        val accountRecord = getRecordByEmail(signUpData.email)
        if (accountRecord != null) throw AccountAlreadyExistsException()

        val newAccount = Account(
            username = signUpData.username,
            email = signUpData.email,
            createdAt = System.currentTimeMillis()
        )
        accounts.add(AccountRecord(newAccount, signUpData.password))
    }

    override fun logout() {
        currentAccountFlow.value = null
    }

    override fun getAccount(): Flow<Account?> = currentAccountFlow

    override suspend fun updateAccountUsername(newUsername: String) {
        if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)

        delay(1000)
        val currentAccount = currentAccountFlow.value ?: throw AuthException()

        val updatedAccount = currentAccount.copy(username = newUsername)
        currentAccountFlow.value = updatedAccount
        val currentRecord = getRecordByEmail(currentAccount.email) ?: throw AuthException()
        currentRecord.account = updatedAccount
    }

    private fun getRecordByEmail(email: String) = accounts.firstOrNull { it.account.email == email }

    private class AccountRecord(
        var account: Account,
        val password: String
    )

}