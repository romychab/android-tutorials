package ua.cn.stu.multimodule.glue.orders.mappers

import ua.cn.stu.multimodule.data.orders.entities.OrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.RecipientDataEntity
import ua.cn.stu.multimodule.formatters.DateTimeFormatter
import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.orders.entities.OrderUsdPrice
import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.entities.OrderItem
import javax.inject.Inject

class OrderMapper @Inject constructor(
    private val orderStatusMapper: OrderStatusMapper,
    private val priceFormatter: PriceFormatter,
    private val dateTimeFormatter: DateTimeFormatter,
){

    fun toOrder(orderDataEntity: OrderDataEntity): Order {
        return Order(
            uuid = orderDataEntity.uuid,
            status = orderStatusMapper.toOrderStatus(orderDataEntity.status),
            createdAt = dateTimeFormatter.formatDateTime(orderDataEntity.createdAtMillis),
            orderDeliverInfo = orderDataEntity.recipient.toDeliverInfo(),
            orderItems = orderDataEntity.items.map {
                OrderItem(
                    id = it.id,
                    name = it.productName,
                    quantity = it.quantity,
                    price = OrderUsdPrice(it.priceUsdCents, priceFormatter)
                )
            }
        )
    }

    private fun RecipientDataEntity.toDeliverInfo(): String {
        return "$firstName $lastName, $address"
    }

}