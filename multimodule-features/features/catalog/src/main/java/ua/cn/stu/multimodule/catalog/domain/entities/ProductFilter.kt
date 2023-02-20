package ua.cn.stu.multimodule.catalog.domain.entities

data class ProductFilter(
    val category: String? = null,
    val minPrice: Price? = null,
    val maxPrice: Price? = null,
    val sortBy: SortBy = SortBy.NAME,
    val sortOrder: SortOrder = SortOrder.ASC,
) : java.io.Serializable {

    companion object {
        val EMPTY = ProductFilter()
    }

}