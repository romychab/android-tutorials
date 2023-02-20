package ua.cn.stu.multimodule.cart.domain

import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.exceptions.QuantityOutOfRangeException
import ua.cn.stu.multimodule.cart.domain.repositories.CartRepository
import ua.cn.stu.multimodule.core.NotFoundException
import javax.inject.Inject

class ChangeCartItemQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val validateQuantityUseCase: ValidateCartItemQuantityUseCase,
) {

    /**
     * Increase the quantity of [cartItem] by +1.
     * @throws NotFoundException
     * @throws QuantityOutOfRangeException
     */
    suspend fun incrementQuantity(cartItem: CartItem) {
        val newQuantity = cartItem.quantity + 1
        changeQuantity(cartItem, newQuantity)
    }

    /**
     * Decrease the quantity of [cartItem] by -1.
     * @throws NotFoundException
     * @throws QuantityOutOfRangeException
     */
    suspend fun decrementQuantity(cartItem: CartItem) {
        val newQuantity = cartItem.quantity - 1
        changeQuantity(cartItem, newQuantity)
    }

    /**
     * Change the quantity of [cartItem] to the [newQuantity] value.
     * @throws NotFoundException
     * @throws QuantityOutOfRangeException
     */
    suspend fun changeQuantity(cartItem: CartItem, newQuantity: Int) {
        validateNewQuantity(cartItem, newQuantity)
        cartRepository.changeQuantity(cartItem.id, newQuantity)
    }

    /**
     * @throws NotFoundException
     * @throws QuantityOutOfRangeException
     */
    private suspend fun validateNewQuantity(cartItem: CartItem, newQuantity: Int) {
        validateQuantityUseCase.validateNewQuantity(cartItem, newQuantity)
    }
}