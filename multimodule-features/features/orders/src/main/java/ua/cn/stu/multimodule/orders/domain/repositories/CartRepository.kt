package ua.cn.stu.multimodule.orders.domain.repositories

import ua.cn.stu.multimodule.orders.domain.entities.Cart

interface CartRepository {

    suspend fun getCart(): Cart

    /**
     * Remove all items from the cart.
     */
    suspend fun cleanUpCart()

}