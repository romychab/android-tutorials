package ua.cn.stu.multimodule.orders.domain

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.repositories.OrdersRepository
import javax.inject.Inject

class GetOrdersUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
) {

    /**
     * Listen for all user's orders.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getOrders(): Flow<Container<List<Order>>> {
        return ordersRepository.getOrders()
    }

    /**
     * Reload the flow returned by [getOrders].
     */
    fun reload() {
        ordersRepository.reload()
    }

}