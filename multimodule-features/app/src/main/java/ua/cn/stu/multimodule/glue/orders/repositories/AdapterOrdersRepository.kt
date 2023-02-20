package ua.cn.stu.multimodule.glue.orders.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.OrdersDataRepository
import ua.cn.stu.multimodule.data.orders.entities.CreateOrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.CreateOrderItemDataEntity
import ua.cn.stu.multimodule.glue.orders.entities.OrderUsdPrice
import ua.cn.stu.multimodule.glue.orders.mappers.OrderMapper
import ua.cn.stu.multimodule.glue.orders.mappers.OrderStatusMapper
import ua.cn.stu.multimodule.glue.orders.mappers.RecipientMapper
import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.domain.entities.OrderStatus
import ua.cn.stu.multimodule.orders.domain.entities.Recipient
import ua.cn.stu.multimodule.orders.domain.repositories.OrdersRepository
import javax.inject.Inject

class AdapterOrdersRepository @Inject constructor(
    private val ordersDataRepository: OrdersDataRepository,
    private val orderStatusMapper: OrderStatusMapper,
    private val orderMapper: OrderMapper,
    private val recipientMapper: RecipientMapper,
) : OrdersRepository {

    override suspend fun makeOrder(cart: Cart, recipient: Recipient) {
        ordersDataRepository.createOrder(CreateOrderDataEntity(
            recipientDataEntity = recipientMapper.toRecipientDataEntity(recipient),
            items = cart.cartItems.map {
                CreateOrderItemDataEntity(
                    productId = it.productId,
                    quantity = it.quantity,
                    priceUsdCents = (it.price as OrderUsdPrice).usdCents
                )
            }
        ))
    }

    override suspend fun changeStatus(orderUuid: String, status: OrderStatus) {
        ordersDataRepository.changeStatus(orderUuid, orderStatusMapper.toOrderStatusDataValue(status))
    }

    override fun getOrders(): Flow<Container<List<Order>>> {
        return ordersDataRepository.getOrders().map { container ->
            container.map { list ->
                list.map { orderDataEntity ->
                    orderMapper.toOrder(orderDataEntity)
                }
            }
        }
    }

    override fun reload() {
        ordersDataRepository.reload()
    }
}