package ua.cn.stu.multimodule.cart.domain.entities

data class Product(
    val id: Long,
    val name: String,
    val shortDetails: String,
    val photo: String,
    val totalQuantity: Int,
)