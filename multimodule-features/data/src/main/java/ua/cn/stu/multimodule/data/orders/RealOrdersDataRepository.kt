package ua.cn.stu.multimodule.data.orders

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.flow.LazyFlowSubjectFactory
import ua.cn.stu.multimodule.data.OrdersDataRepository
import ua.cn.stu.multimodule.data.orders.entities.CreateOrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.OrderDataEntity
import ua.cn.stu.multimodule.data.orders.entities.OrderStatusDataValue
import ua.cn.stu.multimodule.data.orders.sources.OrdersDataSource
import javax.inject.Inject

class RealOrdersDataRepository @Inject constructor(
    private val ordersDataSource: OrdersDataSource,
    lazyFlowSubjectFactory: LazyFlowSubjectFactory,
) : OrdersDataRepository {

    private val ordersSubject = lazyFlowSubjectFactory.create {
        delay(1000)
        ordersDataSource.getOrders()
    }

    override fun getOrders(): Flow<Container<List<OrderDataEntity>>> {
        return ordersSubject.listen()
    }

    override fun reload() {
        ordersSubject.newAsyncLoad()
    }

    override suspend fun changeStatus(orderUuid: String, status: OrderStatusDataValue) {
        ordersDataSource.setStatus(orderUuid, status)
        ordersSubject.newAsyncLoad(silently = true)
    }

    override suspend fun createOrder(data: CreateOrderDataEntity) {
        ordersDataSource.createOrder(data)
        ordersSubject.newAsyncLoad(silently = true)
    }
}