package ua.cn.stu.http.app.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.utils.MutableLiveEvent
import ua.cn.stu.http.app.utils.publishEvent
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.Singletons

/**
 * SplashViewModel checks whether user is signed-in or not.
 */
class SplashViewModel(
    private val accountsRepository: AccountsRepository = Singletons.accountsRepository
) : ViewModel() {

    private val _launchMainScreenEvent = MutableLiveEvent<Boolean>()
    val launchMainScreenEvent = _launchMainScreenEvent.share()

    init {
        viewModelScope.launch {
            _launchMainScreenEvent.publishEvent(accountsRepository.isSignedIn())
        }
    }
}