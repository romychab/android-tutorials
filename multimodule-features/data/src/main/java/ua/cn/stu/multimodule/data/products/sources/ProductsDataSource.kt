package ua.cn.stu.multimodule.data.products.sources

import ua.cn.stu.multimodule.data.products.entities.ProductDataEntity
import ua.cn.stu.multimodule.data.products.entities.ProductDataFilter

interface ProductsDataSource {

    suspend fun getProducts(filter: ProductDataFilter): List<ProductDataEntity>

    suspend fun getProductById(id: Long): ProductDataEntity

    suspend fun getAllCategories(): List<String>

    suspend fun getDiscountPriceUsdCentsForEntity(product: ProductDataEntity): Int?

    suspend fun changeQuantityBy(id: Long, quantityBy: Int)

}