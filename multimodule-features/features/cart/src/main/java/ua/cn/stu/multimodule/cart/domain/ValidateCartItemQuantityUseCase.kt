package ua.cn.stu.multimodule.cart.domain

import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.exceptions.QuantityOutOfRangeException
import ua.cn.stu.multimodule.cart.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.core.NotFoundException
import javax.inject.Inject

class ValidateCartItemQuantityUseCase @Inject constructor(
    private val productsRepository: ProductsRepository
) {

    /**
     * Validate a new quantity for the specified [cartItem].
     * @throws NotFoundException
     * @throws QuantityOutOfRangeException
     */
    suspend fun validateNewQuantity(cartItem: CartItem, newQuantity: Int) {
        if (newQuantity > getMaxQuantity(cartItem)) throw QuantityOutOfRangeException()
        if (newQuantity < 1) throw QuantityOutOfRangeException()
    }

    /**
     * Get max available quantity for the specified [cartItem]
     * @throws NotFoundException
     */
    suspend fun getMaxQuantity(cartItem: CartItem): Int {
        return productsRepository.getAvailableQuantity(cartItem.product.id)
    }

}