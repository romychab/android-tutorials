package ua.cn.stu.multimodule.core.impl

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.core.*

class DefaultErrorHandler(
    private val logger: Logger,
    private val commonUi: CommonUi,
    private val resources: Resources,
    private val appRestarter: AppRestarter,
    private val globalScope: CoroutineScope,
) : ErrorHandler {

    private var lastRestartTimestamp = 0L

    override fun handleError(exception: Throwable) {
        logger.err(exception)
        when (exception) {
            is AuthException -> handleAuthException(exception)
            is ConnectionException -> handleConnectionException(exception)
            is StorageException -> handleStorageException(exception)
            is RemoteServiceException -> handleRemoteServiceException(exception)
            is UserFriendlyException -> handleUserFriendlyException(exception)
            is TimeoutCancellationException -> handleTimeoutException(exception)
            is CancellationException -> return
            else -> handleUnknownException()
        }
    }

    override fun getUserMessage(exception: Throwable): String {
        return when (exception) {
            is AuthException -> resources.getString(R.string.core_common_session_expired)
            is ConnectionException -> resources.getString(R.string.core_common_error_connection)
            is StorageException -> resources.getString(R.string.core_common_error_storage)
            is TimeoutCancellationException -> resources.getString(R.string.core_common_error_timeout)
            is RemoteServiceException -> getRemoteServiceExceptionMessage(exception)
            is UserFriendlyException -> exception.userFriendlyMessage
            else -> resources.getString(R.string.core_common_error_message)
        }
    }

    private fun getRemoteServiceExceptionMessage(exception: RemoteServiceException): String {
        val message = exception.message
        return if (message?.isNotBlank() == true) {
            message
        } else {
            resources.getString(R.string.core_common_error_service)
        }
    }

    private fun handleAuthException(exception: AuthException) {
        val currentTimestamp = System.currentTimeMillis()
        if (currentTimestamp - lastRestartTimestamp > RESTART_TIMEOUT) {
            commonUi.toast(getUserMessage(exception))
            lastRestartTimestamp = currentTimestamp
            appRestarter.restartApp()
        }
    }

    private fun handleConnectionException(exception: ConnectionException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleStorageException(exception: StorageException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleTimeoutException(exception: TimeoutCancellationException) {
        commonUi.toast(getUserMessage(exception))
    }

    private fun handleRemoteServiceException(exception: RemoteServiceException) {
        showErrorDialog(getRemoteServiceExceptionMessage(exception))
    }

    private fun handleUserFriendlyException(exception: UserFriendlyException) {
        showErrorDialog(exception.userFriendlyMessage)
    }

    private fun handleUnknownException() {
        showErrorDialog(resources.getString(R.string.core_common_error_message))
    }

    private fun showErrorDialog(message: String) {
        globalScope.launch {
            commonUi.alertDialog(
                AlertDialogConfig(
                    title = resources.getString(R.string.core_common_error_title),
                    message = message,
                    positiveButton = resources.getString(R.string.core_common_action_ok),
                )
            )
        }
    }

    private companion object {
        const val RESTART_TIMEOUT = 5000
    }
}