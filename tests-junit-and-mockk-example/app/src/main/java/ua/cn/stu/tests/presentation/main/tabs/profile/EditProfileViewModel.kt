package ua.cn.stu.tests.presentation.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ua.cn.stu.tests.R
import ua.cn.stu.tests.domain.EmptyFieldException
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.presentation.base.BaseViewModel
import ua.cn.stu.tests.utils.MutableLiveEvent
import ua.cn.stu.tests.utils.MutableUnitLiveEvent
import ua.cn.stu.tests.utils.logger.Logger
import ua.cn.stu.tests.utils.publishEvent
import ua.cn.stu.tests.utils.share
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger) {

    private val _initialUsernameEvent = MutableLiveEvent<String>()
    val initialUsernameEvent = _initialUsernameEvent.share()

    private val _saveInProgress = MutableLiveData(false)
    val saveInProgress = _saveInProgress.share()

    private val _goBackEvent = MutableUnitLiveEvent()
    val goBackEvent = _goBackEvent.share()

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
            showErrorMessage(R.string.field_is_empty)
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

}