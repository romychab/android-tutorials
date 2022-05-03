package ua.cn.stu.http.sources.accounts.entities

/**
 * Request body for `POST /sign-up` HTTP-request for creating a new account.
 *
 * JSON example:
 * ```
 * {
 *   "email": "",
 *   "username": "",
 *   "password": "",
 * }
 * ```
 */
data class SignUpRequestEntity(
    val email: String,
    val username: String,
    val password: String
)