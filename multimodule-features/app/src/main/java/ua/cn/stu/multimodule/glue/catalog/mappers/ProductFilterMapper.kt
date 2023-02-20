package ua.cn.stu.multimodule.glue.catalog.mappers

import ua.cn.stu.multimodule.catalog.domain.entities.ProductFilter
import ua.cn.stu.multimodule.data.products.entities.ProductDataFilter
import ua.cn.stu.multimodule.glue.catalog.entities.CatalogUsdPrice
import javax.inject.Inject

class ProductFilterMapper @Inject constructor(
    private val sortOrderMapper: SortOrderMapper,
    private val sortByMapper: SortByMapper,
) {

    fun toProductDataFilter(filter: ProductFilter): ProductDataFilter {
        return ProductDataFilter(
            category = filter.category,
            minPriceUsdCents = (filter.minPrice as? CatalogUsdPrice)?.usdCents,
            maxPriceUsdCents = (filter.maxPrice as? CatalogUsdPrice)?.usdCents,
            sortBy = sortByMapper.toSortByDataValue(filter.sortBy),
            sortOrder = sortOrderMapper.toSortOrderDataValue(filter.sortOrder),
        )
    }

}