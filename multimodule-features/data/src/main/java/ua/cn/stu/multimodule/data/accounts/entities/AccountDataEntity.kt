package ua.cn.stu.multimodule.data.accounts.entities

data class AccountDataEntity(
    val id: Long,
    val email: String,
    val username: String,
    val createdAtMillis: Long
)