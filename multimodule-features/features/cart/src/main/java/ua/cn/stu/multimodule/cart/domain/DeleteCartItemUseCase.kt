package ua.cn.stu.multimodule.cart.domain

import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.repositories.CartRepository
import ua.cn.stu.multimodule.core.NotFoundException
import javax.inject.Inject

class DeleteCartItemUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {

    /**
     * Delete the list of cart items.
     * @throws NotFoundException
     */
    suspend fun deleteCartItems(cartItems: List<CartItem>) {
        cartRepository.deleteCartItems(cartItems.map { it.id })
    }

}