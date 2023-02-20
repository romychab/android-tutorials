package ua.cn.stu.multimodule.data

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.NotFoundException
import ua.cn.stu.multimodule.data.products.entities.ProductDataEntity
import ua.cn.stu.multimodule.data.products.entities.ProductDataFilter

interface ProductsDataRepository {

    /**
     * Listen for products that match the specified filter.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getProducts(filter: ProductDataFilter): Flow<Container<List<ProductDataEntity>>>

    /**
     * Change the quantity by a value for product with the specified ID.
     * @throws NotFoundException
     */
    suspend fun changeQuantityBy(id: Long, quantityBy: Int)

    /**
     * Get product by ID.
     * @throws NotFoundException
     */
    suspend fun getProductById(id: Long): ProductDataEntity

    /**
     * Get min product price.
     */
    suspend fun getMinPriceUsdCents(): Int

    /**
     * Get max product price.
     */
    suspend fun getMaxPriceUsdCents(): Int

    /**
     * Get price with discount for the specified product.
     */
    suspend fun getDiscountPriceUsdCentsForEntity(product: ProductDataEntity): Int?

    /**
     * Get all available categories.
     */
    suspend fun getAllCategories(): List<String>

}