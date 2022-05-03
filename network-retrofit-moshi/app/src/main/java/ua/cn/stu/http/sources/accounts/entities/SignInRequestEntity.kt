package ua.cn.stu.http.sources.accounts.entities

/**
 * Request body for `POST /sign-in` HTTP-request.
 *
 * JSON example:
 * ```
 * {
 *   "email": "",
 *   "password": "",
 * }
 * ```
 */
data class SignInRequestEntity(
    val email: String,
    val password: String
)