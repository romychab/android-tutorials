package ua.cn.stu.tabs.model

open class AppException : RuntimeException()

class EmptyFieldException(
    val field: Field
) : AppException()

class PasswordMismatchException : AppException()

class AccountAlreadyExistsException : AppException()

class AuthException : AppException()

class StorageException: AppException()