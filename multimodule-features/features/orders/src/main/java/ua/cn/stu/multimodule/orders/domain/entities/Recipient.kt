package ua.cn.stu.multimodule.orders.domain.entities

data class Recipient(
    val firstName: String,
    val lastName: String,
    val address: String,
    val comment: String,
)