package ua.cn.stu.multimodule.orders.domain

import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.repositories.CartRepository
import javax.inject.Inject

class GetCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {

    /**
     * Get the current user's cart.
     */
    suspend fun getCart(): Cart {
        return cartRepository.getCart()
    }

}