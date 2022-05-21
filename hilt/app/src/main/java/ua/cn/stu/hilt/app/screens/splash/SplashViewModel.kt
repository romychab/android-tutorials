package ua.cn.stu.hilt.app.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.cn.stu.hilt.app.model.accounts.AccountsRepository
import ua.cn.stu.hilt.app.utils.MutableLiveEvent
import ua.cn.stu.hilt.app.utils.publishEvent
import ua.cn.stu.hilt.app.utils.share
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