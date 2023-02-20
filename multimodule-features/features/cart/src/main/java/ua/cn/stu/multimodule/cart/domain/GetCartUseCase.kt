package ua.cn.stu.multimodule.cart.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.cart.domain.entities.Cart
import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.repositories.CartRepository
import ua.cn.stu.multimodule.cart.domain.factories.PriceFactory
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.NotFoundException
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val priceFactory: PriceFactory,
) {

    /**
     * Listen for user's Cart.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getCart(): Flow<Container<Cart>> {
        return cartRepository.getCartItems().map { container ->
            container.map {
                Cart(it, priceFactory)
            }
        }
    }

    /**
     * Get cart item by ID.
     * @throws NotFoundException
     */
    suspend fun getCartById(cartItemId: Long): CartItem {
        return cartRepository.getCartItemById(cartItemId)
    }

    /**
     * Reload Cart flow returned by [getCart].
     */
    fun reload() {
        cartRepository.reload()
    }

}