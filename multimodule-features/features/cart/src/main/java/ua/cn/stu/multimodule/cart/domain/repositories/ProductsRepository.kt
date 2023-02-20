package ua.cn.stu.multimodule.cart.domain.repositories

import ua.cn.stu.multimodule.core.NotFoundException

interface ProductsRepository {

    /**
     * @throws NotFoundException
     */
    suspend fun getAvailableQuantity(productId: Long): Int

}