package ua.cn.stu.room.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ua.cn.stu.room.model.accounts.AccountsRepository
import ua.cn.stu.room.utils.MutableLiveEvent
import ua.cn.stu.room.utils.publishEvent
import ua.cn.stu.room.utils.share

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