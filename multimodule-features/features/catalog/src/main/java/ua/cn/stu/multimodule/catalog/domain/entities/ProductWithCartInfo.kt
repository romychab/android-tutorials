package ua.cn.stu.multimodule.catalog.domain.entities

data class ProductWithCartInfo(
    val product: Product,
    val isInCart: Boolean,
)
