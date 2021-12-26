package ua.cn.stu.room.model.accounts.entities

/**
 * Information about the user.
 */
data class Account(
    val id: Long,
    val username: String,
    val email: String,
    val createdAt: Long = UNKNOWN_CREATED_AT
) {

    /**
     * Let's assume that there is only one admin and its ID = 1 xD
     */
    fun isAdmin() = id == ADMIN_ACCOUNT_ID

    companion object {
        const val UNKNOWN_CREATED_AT = 0L

        private const val ADMIN_ACCOUNT_ID = 1L
    }
}