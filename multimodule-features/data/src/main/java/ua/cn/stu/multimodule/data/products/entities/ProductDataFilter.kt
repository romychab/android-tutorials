package ua.cn.stu.multimodule.data.products.entities

data class ProductDataFilter(
    val category: String? = null,
    val minPriceUsdCents: Int? = null,
    val maxPriceUsdCents: Int? = null,
    val sortBy: SortByDataValue = SortByDataValue.NAME,
    val sortOrder: SortOrderDataValue = SortOrderDataValue.ASC,
) {
    companion object {
        val DEFAULT = ProductDataFilter()
    }
}