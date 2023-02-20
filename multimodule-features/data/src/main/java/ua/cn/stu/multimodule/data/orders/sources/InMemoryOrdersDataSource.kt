package ua.cn.stu.multimodule.data.orders.sources

import com.github.javafaker.Faker
import kotlinx.coroutines.delay
import ua.cn.stu.multimodule.core.NotFoundException
import ua.cn.stu.multimodule.data.orders.entities.*
import ua.cn.stu.multimodule.data.products.sources.ProductsDataSource
import java.util.Random
import java.util.UUID
import javax.inject.Inject

class InMemoryOrdersDataSource @Inject constructor(
    private val productsDataSource: ProductsDataSource,
) : OrdersDataSource {

    private val faker = Faker.instance(Random(42))
    private val random = Random(42)

    private val orders = createOrders()

    override suspend fun getOrders(): List<OrderDataEntity> {
        return orders.sortedByDescending { it.createdAtMillis }
    }

    override suspend fun setStatus(uuid: String, newStatus: OrderStatusDataValue) {
        val index = orders.indexOfFirst { it.uuid == uuid }
        if (index == -1) throw NotFoundException()
        delay(1000)
        val updatedOrder = orders[index].copy(
            status = newStatus
        )
        orders[index] = updatedOrder
    }

    override suspend fun createOrder(createOrderDataEntity: CreateOrderDataEntity) {
        delay(1000)
        val newOrder = OrderDataEntity(
            uuid = UUID.randomUUID().toString().substring(0, 18),
            recipient = createOrderDataEntity.recipientDataEntity,
            status = OrderStatusDataValue.CREATED,
            createdAtMillis = System.currentTimeMillis(),
            items = createOrderDataEntity.items.map {
                OrderItemDataEntity(
                    id = UUID.randomUUID().toString(),
                    productName = productsDataSource.getProductById(it.productId).name,
                    quantity = it.quantity,
                    priceUsdCents = it.priceUsdCents,
                )
            }
        )
        orders.add(newOrder)
    }

    private fun createOrders(): MutableList<OrderDataEntity> {
        return MutableList(7) {
            OrderDataEntity(
                uuid = UUID.randomUUID().toString().substring(0, 18),
                recipient = RecipientDataEntity(
                    firstName = faker.name().firstName(),
                    lastName = faker.name().lastName(),
                    address = faker.address().fullAddress()
                ),
                status = OrderStatusDataValue.values()[it % OrderStatusDataValue.values().size],
                createdAtMillis = System.currentTimeMillis() - 1000 * 3600 * 6 * (random.nextInt(10) + 10),
                items = List(random.nextInt(5) + 1) {
                    OrderItemDataEntity(
                        id = UUID.randomUUID().toString(),
                        productName = faker.food().dish(),
                        quantity = random.nextInt(6) + 1,
                        priceUsdCents = 1000 + random.nextInt(20000)
                    )
                }
            )
        }
    }

}