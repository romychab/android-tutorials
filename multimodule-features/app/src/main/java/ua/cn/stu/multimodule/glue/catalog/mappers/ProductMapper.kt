package ua.cn.stu.multimodule.glue.catalog.mappers

import ua.cn.stu.multimodule.catalog.domain.entities.Product
import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.data.products.entities.ProductDataEntity
import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.catalog.entities.CatalogUsdPrice
import javax.inject.Inject

class ProductMapper @Inject constructor(
    private val productsDataRepository: ProductsDataRepository,
    private val priceFormatter: PriceFormatter,
) {

    suspend fun toProduct(dataEntity: ProductDataEntity): Product {
        val discountPrice = productsDataRepository.getDiscountPriceUsdCentsForEntity(dataEntity)?.let {
            CatalogUsdPrice(it, priceFormatter)
        }
        return Product(
            id = dataEntity.id,
            name = dataEntity.name,
            shortDetails = dataEntity.shortDescription,
            details = dataEntity.description,
            category = dataEntity.category,
            price = CatalogUsdPrice(dataEntity.priceUsdCents, priceFormatter),
            priceWithDiscount = discountPrice,
            photo = dataEntity.imageUrl
        )
    }

}