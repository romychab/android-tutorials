package ua.cn.stu.http.app.model.accounts

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.http.app.model.*
import ua.cn.stu.http.app.utils.async.LazyFlowSubject
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.model.accounts.entities.SignUpData
import ua.cn.stu.http.app.model.settings.AppSettings

class AccountsRepository(
    private val accountsSource: AccountsSource,
    private val appSettings: AppSettings
) {

    private val accountLazyFlowSubject = LazyFlowSubject<Unit, Account> {
        doGetAccount()
    }

    /**
     * Whether user is signed-in or not.
     */
    fun isSignedIn(): Boolean {
        // user is signed-in if auth token exists
        return appSettings.getCurrentToken() != null
    }

    /**
     * Try to sign-in with the email and password.
     * @throws EmptyFieldException
     * @throws InvalidCredentialsException
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)

        val token = try {
            accountsSource.signIn(email, password)
        } catch (e: Exception) {
            if (e is BackendException && e.code == 401) {
                // map 401 error for sign-in to InvalidCredentialsException
                throw InvalidCredentialsException(e)
            } else {
                throw e
            }
        }
        // success! got auth token -> save it
        appSettings.setCurrentToken(token)
        // and load account data
        accountLazyFlowSubject.updateAllValues(accountsSource.getAccount())
    }

    /**
     * Create a new account.
     * @throws EmptyFieldException
     * @throws PasswordMismatchException
     * @throws AccountAlreadyExistsException
     * @throws ConnectionException
     * @throws BackendException
     */
    suspend fun signUp(signUpData: SignUpData) {
        signUpData.validate()
        try {
            accountsSource.signUp(signUpData)
        } catch (e: BackendException) {
            // user with such email already exists
            if (e.code == 409) throw AccountAlreadyExistsException(e)
            else throw e
        }
    }


    /**
     * Reload account info. Results of reloading are delivered to the flows
     * returned by [getAccount] method.
     */
    fun reloadAccount() {
        accountLazyFlowSubject.reloadAll()
    }

    /**
     * Get the account info of the current signed-in user and listen for all
     * further changes of the account data.
     * If user is not logged-in an empty result is emitted.
     * @return infinite flow, always success; errors are wrapped to [Result]
     */
    fun getAccount(): Flow<Result<Account>> {
        return accountLazyFlowSubject.listen(Unit)
    }

    /**
     * Change the username of the current signed-in user. An updated account
     * entity is delivered to the flow returned by [getAccount] call after
     * calling this method.
     * @throws EmptyFieldException
     * @throws AuthException
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun updateAccountUsername(newUsername: String) = wrapBackendExceptions {
        if (newUsername.isBlank()) throw EmptyFieldException(Field.Username)
        accountsSource.setUsername(newUsername)
        accountLazyFlowSubject.updateAllValues(accountsSource.getAccount())
    }

    /**
     * Clear JWT-token saved in settings.
     */
    fun logout() {
        appSettings.setCurrentToken(null)
        accountLazyFlowSubject.updateAllValues(null)
    }

    private suspend fun doGetAccount(): Account = wrapBackendExceptions {
        try {
            accountsSource.getAccount()
        } catch (e: BackendException) {
            // account has been deleted = session expired = AuthException
            if (e.code == 404) throw AuthException(e)
            else throw e
        }
    }

}