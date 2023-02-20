package ua.cn.stu.multimodule.orders.domain

import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.entities.Field
import ua.cn.stu.multimodule.orders.domain.entities.Recipient
import ua.cn.stu.multimodule.orders.domain.exceptions.EmptyFieldException
import ua.cn.stu.multimodule.orders.domain.repositories.CartRepository
import ua.cn.stu.multimodule.orders.domain.repositories.OrdersRepository
import ua.cn.stu.multimodule.orders.domain.repositories.ProductsRepository
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val ordersRepository: OrdersRepository,
    private val cartRepository: CartRepository,
    private val productsRepository: ProductsRepository,
) {

    /**
     * Create a new order.
     * @throws EmptyFieldException
     */
    suspend fun createOrder(cart: Cart, recipient: Recipient) {
        if (recipient.firstName.isBlank()) throw EmptyFieldException(Field.FIRST_NAME)
        if (recipient.lastName.isBlank()) throw EmptyFieldException(Field.LAST_NAME)
        if (recipient.address.isBlank()) throw EmptyFieldException(Field.ADDRESS)

        ordersRepository.makeOrder(cart, recipient)
        cartRepository.cleanUpCart()
        cart.cartItems.forEach { cartItem ->
            productsRepository.changeQuantityBy(cartItem.productId, -cartItem.quantity)
        }
    }

}