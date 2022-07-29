package ua.cn.stu.tests.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.utils.MutableLiveEvent
import ua.cn.stu.tests.utils.publishEvent
import ua.cn.stu.tests.utils.share
import javax.inject.Inject

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountsRepository: AccountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}