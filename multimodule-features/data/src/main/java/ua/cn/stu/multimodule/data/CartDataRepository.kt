package ua.cn.stu.multimodule.data

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.NotFoundException
import ua.cn.stu.multimodule.data.cart.entities.CartItemDataEntity

interface CartDataRepository {

    /**
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getCart(): Flow<Container<List<CartItemDataEntity>>>

    /**
     * Add a new product to the cart.
     * @throws NotFoundException
     */
    suspend fun addToCart(productId: Long, quantity: Int)

    /**
     * Get cart item by its ID.
     * @throws NotFoundException
     */
    suspend fun getCartItemById(id: Long): CartItemDataEntity

    /**
     * Delete the specified cart items.
     */
    suspend fun deleteCartItem(ids: List<Long>)

    /**
     * Delete all items in the cart.
     */
    suspend fun deleteAll()

    /**
     * Change quantity of the specified cart item.
     * @throws NotFoundException
     */
    suspend fun changeQuantity(cartId: Long, quantity: Int)

    /**
     * Reload the flow returned by [getCart]
     */
    fun reload()

}