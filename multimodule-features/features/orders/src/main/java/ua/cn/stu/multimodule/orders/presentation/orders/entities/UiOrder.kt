package ua.cn.stu.multimodule.orders.presentation.orders.entities

import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.entities.OrderItem
import ua.cn.stu.multimodule.orders.domain.entities.OrderStatus

data class UiOrder(
    val origin: Order,
    val canCancel: Boolean,
    val cancelInProgress: Boolean,
) {

    val uuid: String get() = origin.uuid
    val createdAt: String get() = origin.createdAt
    val recipient: String get() = origin.orderDeliverInfo
    val orderItems: List<OrderItem> get() = origin.orderItems
    val status: OrderStatus get() = origin.status

}