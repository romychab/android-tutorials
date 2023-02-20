package ua.cn.stu.multimodule.data.products.sources

interface DiscountsDataSource {

    suspend fun getDiscountPercentage(productId: Long): Int?

}