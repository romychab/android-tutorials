package ua.cn.stu.multimodule.core

/**
 * Any in-app managed exceptions.
 */
open class AppException(
    message: String = "",
    cause: Throwable? = null,
) : Exception(message, cause)

/**
 * Problems with internet connection
 */
class ConnectionException(cause: Exception) : AppException(cause = cause)

/**
 * Problems with remote service
 */
open class RemoteServiceException(
    message: String,
    cause: Exception? = null
) : AppException(message, cause)

/**
 * Authentication problems.
 */
class AuthException(cause: Exception? = null) : AppException(cause = cause)

/**
 * Problems with reading/writing data to a local storage.
 */
class StorageException(cause: Exception) : AppException(cause = cause)

/**
 * Exception with user-friendly message which can be safely displayed to the user.
 */
class UserFriendlyException(
    val userFriendlyMessage: String,
    cause: Exception,
) : AppException(cause.message ?: "", cause)

/**
 * Something doesn't exist
 */
class NotFoundException : AppException()
