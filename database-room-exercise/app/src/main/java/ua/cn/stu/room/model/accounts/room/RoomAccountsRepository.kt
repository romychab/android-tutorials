package ua.cn.stu.room.model.accounts.room

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import ua.cn.stu.room.model.AuthException
import ua.cn.stu.room.model.EmptyFieldException
import ua.cn.stu.room.model.Field
import ua.cn.stu.room.model.accounts.AccountsRepository
import ua.cn.stu.room.model.accounts.entities.Account
import ua.cn.stu.room.model.accounts.entities.SignUpData
import ua.cn.stu.room.model.room.wrapSQLiteException
import ua.cn.stu.room.model.settings.AppSettings
import ua.cn.stu.room.utils.AsyncLoader

class RoomAccountsRepository(
    private val accountsDao: AccountsDao,
    private val appSettings: AppSettings,
    private val ioDispatcher: CoroutineDispatcher
) : AccountsRepository {

    private val currentAccountIdFlow = AsyncLoader {
        MutableStateFlow(AccountId(appSettings.getCurrentAccountId()))
    }

    override suspend fun isSignedIn(): Boolean {
        delay(2000)
        return appSettings.getCurrentAccountId() != AppSettings.NO_ACCOUNT_ID
    }

    override suspend fun signIn(email: String, password: String) = wrapSQLiteException(ioDispatcher) {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)

        delay(1000)

        val accountId = findAccountIdByEmailAndPassword(email, password)
        appSettings.setCurrentAccountId(accountId)
        currentAccountIdFlow.get().value = AccountId(accountId)

        return@wrapSQLiteException
    }

    override suspend fun signUp(signUpData: SignUpData) = wrapSQLiteException(ioDispatcher) {
        signUpData.validate()
        delay(1000)
        createAccount(signUpData)
    }

    override suspend fun logout() {
        appSettings.setCurrentAccountId(AppSettings.NO_ACCOUNT_ID)
        currentAccountIdFlow.get().value = AccountId(AppSettings.NO_ACCOUNT_ID)
    }

    override suspend fun getAccount(): Flow<Account?> {
        return currentAccountIdFlow.get()
            .flatMapLatest { accountId ->
                if (accountId.value == AppSettings.NO_ACCOUNT_ID) {
                    flowOf(null)
                } else {
                    getAccountById(accountId.value)
                }
            }
            .flowOn(ioDispatcher)
    }

    override suspend fun updateAccountUsername(newUsername: String) = wrapSQLiteException(ioDispatcher) {
        if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
        delay(1000)
        val accountId = appSettings.getCurrentAccountId()
        if (accountId == AppSettings.NO_ACCOUNT_ID) throw AuthException()

        updateUsernameForAccountId(accountId, newUsername)

        currentAccountIdFlow.get().value = AccountId(accountId)
        return@wrapSQLiteException
    }

    private suspend fun findAccountIdByEmailAndPassword(email: String, password: String): Long {
        TODO("#11: use AccountsDao to fetch ID and Password by Email. " +
                "Throw AuthException if there is no account with such email or password is invalid.")
    }

    private suspend fun createAccount(signUpData: SignUpData) {
        // todo #12: create a new AccountDbEntity from SignUpData here and insert it to the database by
        //           using AccountsDao.
        //           Catch SQLiteConstraintException and rethrow AccountAlreadyExistsException.
        //           SQLiteConstraintException is thrown by DAO in case if there is another
        //           account with the same email address
    }

    private fun getAccountById(accountId: Long): Flow<Account?> {
        TODO("#13: get account info by ID; do not forget to map AccountDbEntity to Account here")
    }

    private suspend fun updateUsernameForAccountId(accountId: Long, newUsername: String) {
        // todo #14: update username for the account with specified ID.
        //           hint: use a tuple class created before (step #7)
    }

    private class AccountId(val value: Long)

}