package ua.cn.stu.multimodule.data.orders.sources

import ua.cn.stu.multimodule.data.orders.entities.CreateOrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.OrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.OrderStatusDataValue

interface OrdersDataSource {

    suspend fun getOrders(): List<OrderDataEntity>

    suspend fun setStatus(uuid: String, newStatus: OrderStatusDataValue)

    suspend fun createOrder(createOrderDataEntity: CreateOrderDataEntity)

}