package ua.cn.stu.multimodule.glue.catalog.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.catalog.domain.entities.Price
import ua.cn.stu.multimodule.catalog.domain.entities.Product
import ua.cn.stu.multimodule.catalog.domain.entities.ProductFilter
import ua.cn.stu.multimodule.catalog.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.catalog.entities.CatalogUsdPrice
import ua.cn.stu.multimodule.glue.catalog.mappers.ProductFilterMapper
import ua.cn.stu.multimodule.glue.catalog.mappers.ProductMapper
import javax.inject.Inject

class AdapterProductsRepository @Inject constructor(
    private val productsDataRepository: ProductsDataRepository,
    private val productMapper: ProductMapper,
    private val productFilterMapper: ProductFilterMapper,
    private val priceFormatter: PriceFormatter,
) : ProductsRepository {

    override fun getProducts(filter: ProductFilter): Flow<Container<List<Product>>> {
        val dataFilter = productFilterMapper.toProductDataFilter(filter)
        return productsDataRepository.getProducts(dataFilter).map { container ->
            container.suspendMap { list ->
                list.map { dataEntity ->
                    productMapper.toProduct(dataEntity)
                }
            }
        }
    }

    override suspend fun getProduct(id: Long): Product {
        val productDataEntity = productsDataRepository.getProductById(id)
        return productMapper.toProduct(productDataEntity)
    }

    override suspend fun getMinPossiblePrice(): Price {
        return CatalogUsdPrice(productsDataRepository.getMinPriceUsdCents(), priceFormatter)
    }

    override suspend fun getMaxPossiblePrice(): Price {
        return CatalogUsdPrice(productsDataRepository.getMaxPriceUsdCents(), priceFormatter)
    }

    override suspend fun getAllCategories(): List<String> {
        return productsDataRepository.getAllCategories()
    }

}