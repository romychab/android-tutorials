package ua.cn.stu.http.app.screens.main.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.screens.base.BaseViewModel
import ua.cn.stu.http.app.utils.*
import ua.cn.stu.http.app.utils.logger.LogCatLogger
import ua.cn.stu.http.app.Singletons
import ua.cn.stu.http.app.model.EmptyFieldException
import ua.cn.stu.http.app.model.Field
import ua.cn.stu.http.app.model.InvalidCredentialsException
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.utils.logger.Logger

class SignInViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _state = MutableLiveData(State())
    val state = _state.share()

    private val _clearPasswordEvent = MutableUnitLiveEvent()
    val clearPasswordEvent = _clearPasswordEvent.share()

    private val _showAuthErrorToastEvent = MutableLiveEvent<Int>()
    val showAuthToastEvent = _showAuthErrorToastEvent.share()

    private val _navigateToTabsEvent = MutableUnitLiveEvent()
    val navigateToTabsEvent = _navigateToTabsEvent.share()

    fun signIn(email: String, password: String) = viewModelScope.safeLaunch {
        showProgress()
        try {
            accountsRepository.signIn(email, password)
            launchTabsScreen()
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: InvalidCredentialsException) {
            processInvalidCredentialsException()
        } finally {
            hideProgress()
        }
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password
        )
    }

    private fun processInvalidCredentialsException() {
        clearPasswordField()
        showAuthErrorToast()
    }

    private fun showProgress() {
        _state.value = State(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(signInInProgress = false)
    }

    private fun clearPasswordField() = _clearPasswordEvent.publishEvent()

    private fun showAuthErrorToast() = _showAuthErrorToastEvent.publishEvent(R.string.invalid_email_or_password)

    private fun launchTabsScreen() = _navigateToTabsEvent.publishEvent()

    data class State(
        val emptyEmailError: Boolean = false,
        val emptyPasswordError: Boolean = false,
        val signInInProgress: Boolean = false
    ) {
        val showProgress: Boolean get() = signInInProgress
        val enableViews: Boolean get() = !signInInProgress
    }
}