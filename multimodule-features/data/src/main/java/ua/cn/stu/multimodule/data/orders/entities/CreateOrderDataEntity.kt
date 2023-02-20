package ua.cn.stu.multimodule.data.orders.entities

data class CreateOrderDataEntity(
    val items: List<CreateOrderItemDataEntity>,
    val recipientDataEntity: RecipientDataEntity,
)