package ua.cn.stu.multimodule.orders.domain.repositories

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.entities.OrderStatus
import ua.cn.stu.multimodule.orders.domain.entities.Recipient

interface OrdersRepository {

    /**
     * Create a new order by using items in the cart.
     */
    suspend fun makeOrder(cart: Cart, recipient: Recipient)

    /**
     * Change status of the specified order.
     */
    suspend fun changeStatus(orderUuid: String, status: OrderStatus)

    /**
     * Listen for order list.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getOrders(): Flow<Container<List<Order>>>

    /**
     * Reload orders flow returned by [getOrders]
     */
    fun reload()
    
}