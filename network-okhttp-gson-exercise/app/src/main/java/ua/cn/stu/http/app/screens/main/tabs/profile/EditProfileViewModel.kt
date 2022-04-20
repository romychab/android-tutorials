package ua.cn.stu.http.app.screens.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.screens.base.BaseViewModel
import ua.cn.stu.http.app.utils.MutableLiveEvent
import ua.cn.stu.http.app.utils.MutableUnitLiveEvent
import ua.cn.stu.http.app.utils.publishEvent
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.utils.logger.LogCatLogger
import ua.cn.stu.http.app.Singletons
import ua.cn.stu.http.app.model.EmptyFieldException
import ua.cn.stu.http.app.model.Success
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.utils.logger.Logger

class EditProfileViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _initialUsernameEvent = MutableLiveEvent<String>()
    val initialUsernameEvent = _initialUsernameEvent.share()

    private val _saveInProgress = MutableLiveData(false)
    val saveInProgress = _saveInProgress.share()

    private val _goBackEvent = MutableUnitLiveEvent()
    val goBackEvent = _goBackEvent.share()

    private val _showErrorEvent = MutableLiveEvent<Int>()
    val showErrorEvent = _showErrorEvent.share()

    init {
        viewModelScope.launch {
            val res = accountsRepository.getAccount()
                .filter { it.isFinished() }
                .first()
            if (res is Success) _initialUsernameEvent.publishEvent(res.value.username)
        }
    }

    fun saveUsername(newUsername: String) = viewModelScope.safeLaunch {
        showProgress()
        try {
            accountsRepository.updateAccountUsername(newUsername)
            goBack()
        } catch (e: EmptyFieldException) {
            showEmptyFieldErrorMessage()
        } finally {
            hideProgress()
        }
    }

    private fun goBack() = _goBackEvent.publishEvent()

    private fun showProgress() {
        _saveInProgress.value = true
    }

    private fun hideProgress() {
        _saveInProgress.value = false
    }

    private fun showEmptyFieldErrorMessage() = _showErrorEvent.publishEvent(R.string.field_is_empty)

}