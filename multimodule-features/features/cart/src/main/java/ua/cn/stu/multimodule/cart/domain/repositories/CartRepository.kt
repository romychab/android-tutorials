package ua.cn.stu.multimodule.cart.domain.repositories

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.NotFoundException


interface CartRepository {

    /**
     * Change the quantity of [cartItemId] to the [newQuantity] value.
     * @throws NotFoundException
     */
    suspend fun changeQuantity(cartItemId: Long, newQuantity: Int)

    /**
     * Delete the specified cart items.
     * @throws NotFoundException
     */
    suspend fun deleteCartItems(cartItemIds: List<Long>)

    /**
     * Get cart item by ID.
     * @throws NotFoundException
     */
    suspend fun getCartItemById(id: Long): CartItem

    /**
     * Listen for user's Cart.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getCartItems(): Flow<Container<List<CartItem>>>

    /**
     * Reload Cart flow returned by [getCartItems].
     */
    fun reload()

}