package ua.cn.stu.navcomponent.tabs.model

open class AppException : RuntimeException()

class EmptyFieldException(
    val field: Field
) : AppException()

class PasswordMismatchException : AppException()

class AccountAlreadyExistsException : AppException()

class AuthException : AppException()