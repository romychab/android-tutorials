package ua.cn.stu.multimodule.data.products.sources

import com.github.javafaker.Faker
import kotlinx.coroutines.runBlocking
import ua.cn.stu.multimodule.core.NotFoundException
import ua.cn.stu.multimodule.data.products.entities.ProductDataEntity
import ua.cn.stu.multimodule.data.products.entities.ProductDataFilter
import ua.cn.stu.multimodule.data.products.entities.SortByDataValue
import ua.cn.stu.multimodule.data.products.entities.SortOrderDataValue
import javax.inject.Inject
import kotlin.random.Random

class InMemoryProductsDataSource @Inject constructor(
    private val discountsDataSource: DiscountsDataSource,
) : ProductsDataSource {

    private val availableImages = listOf(
        "https://images.unsplash.com/photo-1546554137-f86b9593a222?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk1ODU&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
        "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk2MDQ&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
        "https://images.unsplash.com/photo-1545093149-618ce3bcf49d?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk2MjI&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
        "https://images.unsplash.com/photo-1454944338482-a69bb95894af?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk2MTU&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
        "https://images.unsplash.com/photo-1486328228599-85db4443971f?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk2NTU&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
        "https://images.unsplash.com/photo-1562967916-eb82221dfb92?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=480&ixid=MnwxfDB8MXxyYW5kb218MHx8Zm9vZHx8fHx8fDE2NzUxNjk2NTA&ixlib=rb-4.0.3&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=640",
    )

    private val faker = Faker.instance(java.util.Random(7))

    private val products = generateRandomProducts()

    override suspend fun getProducts(filter: ProductDataFilter): List<ProductDataEntity> {
        val filteredList = products.filter { filterProduct(it, filter) }
        val sortedList = when (filter.sortBy) {
            SortByDataValue.NAME -> filteredList.sortedBy { it.name }
            SortByDataValue.PRICE -> filteredList.sortedBy {
                runBlocking {
                    getDiscountPriceUsdCentsForEntity(it) ?: it.priceUsdCents
                }
            }
        }
        return if (filter.sortOrder == SortOrderDataValue.DESC) {
            sortedList.reversed()
        } else {
            sortedList
        }
    }

    override suspend fun getProductById(id: Long): ProductDataEntity {
        return products.firstOrNull { it.id == id } ?: throw NotFoundException()
    }

    override suspend fun getAllCategories(): List<String> {
        return products.map { it.category }.distinct()
    }

    override suspend fun getDiscountPriceUsdCentsForEntity(product: ProductDataEntity): Int? {
        val percentage = discountsDataSource.getDiscountPercentage(product.id) ?: return null
        val discountProportion = 1 - percentage / 100.0
        return (discountProportion * product.priceUsdCents).toInt()
    }

    override suspend fun changeQuantityBy(id: Long, quantityBy: Int) {
        val index = products.indexOfFirst { it.id == id }
        if (index == -1) throw NotFoundException()
        val oldProduct = products[index]
        val newQuantity = oldProduct.quantityAvailable + quantityBy
        if (newQuantity > 0) {
            products[index] = oldProduct.copy(
                quantityAvailable = newQuantity
            )
        } else {
            products.removeAt(index)
        }
    }

    private fun generateRandomProducts(): MutableList<ProductDataEntity> {
        val categories = listOf(
            "Dishes",
            "Desserts",
            "Vegetables",
            "Fruits",
            "Meat & Cheese"
        )
        val random = Random(0)
        var idSequence: Long = 0
        return categories.flatMap { category ->
            List(random.nextInt(10, 30)) {
                ProductDataEntity(
                    id = ++idSequence,
                    name = "${faker.food().dish()} ${faker.food().ingredient()}",
                    category = category,
                    shortDescription = faker.lorem().paragraph(2),
                    description = faker.lorem().paragraph(8),
                    imageUrl = availableImages[idSequence.toInt() % availableImages.size],
                    quantityAvailable = random.nextInt(10, 60),
                    priceUsdCents = random.nextInt(15, 100) * 100 + 99
                )
            }
        }.shuffled().toMutableList()
    }

    private suspend fun filterProduct(product: ProductDataEntity, filter: ProductDataFilter): Boolean {
        if (filter.category != null && product.category != filter.category) return false
        if (filter.minPriceUsdCents != null) {
            val price = getDiscountPriceUsdCentsForEntity(product) ?: product.priceUsdCents
            if (price < filter.minPriceUsdCents) return false
        }
        if (filter.maxPriceUsdCents != null) {
            val price = getDiscountPriceUsdCentsForEntity(product) ?: product.priceUsdCents
            if (price > filter.maxPriceUsdCents) return false
        }
        return true
    }

}