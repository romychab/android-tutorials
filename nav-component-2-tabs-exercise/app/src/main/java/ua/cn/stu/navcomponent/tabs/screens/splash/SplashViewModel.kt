package ua.cn.stu.navcomponent.tabs.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.cn.stu.navcomponent.tabs.model.accounts.AccountsRepository
import ua.cn.stu.navcomponent.tabs.utils.MutableLiveEvent
import ua.cn.stu.navcomponent.tabs.utils.publishEvent
import ua.cn.stu.navcomponent.tabs.utils.share

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
class SplashViewModel(
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