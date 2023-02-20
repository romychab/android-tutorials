package ua.cn.stu.multimodule.orders.domain

import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.entities.OrderStatus
import ua.cn.stu.multimodule.orders.domain.exceptions.InvalidOrderStatusException
import ua.cn.stu.multimodule.orders.domain.repositories.OrdersRepository
import javax.inject.Inject

class CancelOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
) {

    /**
     * Cancel the specified order.
     * @throws InvalidOrderStatusException
     */
    suspend fun cancelOrder(order: Order) {
        if (!canCancel(order)) {
            throw InvalidOrderStatusException()
        }
        ordersRepository.changeStatus(order.uuid, OrderStatus.CANCELLED)
    }

    /**
     * Whether the order can be cancelled or not.
     */
    fun canCancel(order: Order): Boolean {
        return order.status == OrderStatus.CREATED
                || order.status == OrderStatus.ACCEPTED
    }

}