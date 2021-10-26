package ua.cn.stu.navcomponent.tabs.model.accounts.entities

import ua.cn.stu.navcomponent.tabs.model.EmptyFieldException
import ua.cn.stu.navcomponent.tabs.model.Field
import ua.cn.stu.navcomponent.tabs.model.PasswordMismatchException

/**
 * Fields that should be provided during creating a new account.
 */
data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {
    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (username.isBlank()) throw EmptyFieldException(Field.Username)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException()
    }
}