package ua.cn.stu.multimodule.glue.cart.repositories

import ua.cn.stu.multimodule.cart.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.data.ProductsDataRepository
import javax.inject.Inject


class AdapterProductsRepository @Inject constructor(
    private val productsDataRepository: ProductsDataRepository,
) : ProductsRepository {

    override suspend fun getAvailableQuantity(productId: Long): Int {
        return productsDataRepository.getProductById(productId).quantityAvailable
    }

}