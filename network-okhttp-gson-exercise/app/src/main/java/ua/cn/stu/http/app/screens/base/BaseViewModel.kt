package ua.cn.stu.http.app.screens.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.utils.MutableLiveEvent
import ua.cn.stu.http.app.utils.MutableUnitLiveEvent
import ua.cn.stu.http.app.utils.publishEvent
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.model.AuthException
import ua.cn.stu.http.app.model.BackendException
import ua.cn.stu.http.app.model.ConnectionException
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.utils.logger.Logger

open class BaseViewModel(
    val accountsRepository: AccountsRepository,
    val logger: Logger
) : ViewModel() {

    private val _showErrorMessageResEvent = MutableLiveEvent<Int>()
    val showErrorMessageResEvent = _showErrorMessageResEvent.share()

    private val _showErrorMessageEvent = MutableLiveEvent<String>()
    val showErrorMessageEvent = _showErrorMessageEvent.share()

    private val _showAuthErrorAndRestartEvent = MutableUnitLiveEvent()
    val showAuthErrorAndRestartEvent = _showAuthErrorAndRestartEvent.share()

    fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: ConnectionException) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.connection_error)
            } catch (e: BackendException) {
                logError(e)
                _showErrorMessageEvent.publishEvent(e.message ?: "")
            } catch (e: AuthException) {
                logError(e)
                _showAuthErrorAndRestartEvent.publishEvent()
            } catch (e: Exception) {
                logError(e)
                _showErrorMessageResEvent.publishEvent(R.string.internal_error)
            }
        }
    }

    fun logError(e: Throwable) {
        logger.error(javaClass.simpleName, e)
    }

    fun logout() {
        accountsRepository.logout()
    }

}