package ua.cn.stu.multimodule.glue.orders.repositories

import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.orders.domain.repositories.ProductsRepository
import javax.inject.Inject

class AdapterProductsRepository @Inject constructor(
    private val productsDataRepository: ProductsDataRepository,
) : ProductsRepository {

    override suspend fun changeQuantityBy(productId: Long, byValue: Int) {
        productsDataRepository.changeQuantityBy(productId, byValue)
    }
}