package ua.cn.stu.multimodule.data.orders.entities

data class OrderDataEntity(
    val uuid: String,
    val recipient: RecipientDataEntity,
    val items: List<OrderItemDataEntity>,
    val status: OrderStatusDataValue,
    val createdAtMillis: Long,
)