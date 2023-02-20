package ua.cn.stu.multimodule.data

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.NotFoundException
import ua.cn.stu.multimodule.data.orders.entities.*

interface OrdersDataRepository {

    /**
     * Listen for all orders.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getOrders(): Flow<Container<List<OrderDataEntity>>>

    /**
     * Reload orders flow returned by [getOrders]
     */
    fun reload()

    /**
     * Change the order status.
     * @throws NotFoundException
     */
    suspend fun changeStatus(orderUuid: String, status: OrderStatusDataValue)

    /**
     * Create a new order.
     * @throws NotFoundException if at least one product ID is unknown
     */
    suspend fun createOrder(data: CreateOrderDataEntity)

}