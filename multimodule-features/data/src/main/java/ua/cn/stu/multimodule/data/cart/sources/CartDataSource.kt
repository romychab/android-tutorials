package ua.cn.stu.multimodule.data.cart.sources

import ua.cn.stu.multimodule.data.cart.entities.CartItemDataEntity

interface CartDataSource {

    suspend fun clearCart()

    suspend fun getCart(): List<CartItemDataEntity>

    suspend fun saveToCart(productId: Long, quantity: Int)

    suspend fun delete(cartItemId: Long)

    suspend fun deleteAll()

}