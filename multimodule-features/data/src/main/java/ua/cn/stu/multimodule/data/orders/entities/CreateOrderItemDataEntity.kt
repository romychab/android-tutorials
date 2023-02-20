package ua.cn.stu.multimodule.data.orders.entities

data class CreateOrderItemDataEntity(
    val productId: Long,
    val quantity: Int,
    val priceUsdCents: Int
)