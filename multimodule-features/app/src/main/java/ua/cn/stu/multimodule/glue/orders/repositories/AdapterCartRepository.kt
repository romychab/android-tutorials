package ua.cn.stu.multimodule.glue.orders.repositories

import ua.cn.stu.multimodule.core.unwrapFirst
import ua.cn.stu.multimodule.data.CartDataRepository
import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.orders.entities.OrderUsdPrice
import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.entities.CartItem
import ua.cn.stu.multimodule.orders.domain.factories.PriceFactory
import ua.cn.stu.multimodule.orders.domain.repositories.CartRepository
import javax.inject.Inject

class AdapterCartRepository @Inject constructor(
    private val cartDataRepository: CartDataRepository,
    private val productsDataRepository: ProductsDataRepository,
    private val priceFormatter: PriceFormatter,
    private val priceFactory: PriceFactory,
) : CartRepository {

    override suspend fun cleanUpCart() {
        cartDataRepository.deleteAll()
    }

    override suspend fun getCart(): Cart {
        val cartItems = cartDataRepository.getCart().unwrapFirst()
        return Cart(
            cartItems = cartItems.map {
                val product = productsDataRepository.getProductById(it.productId)
                val priceWithDiscount = productsDataRepository.getDiscountPriceUsdCentsForEntity(product)
                    ?: product.priceUsdCents
                CartItem(
                    name = product.name,
                    productId = it.productId,
                    price = OrderUsdPrice(priceWithDiscount, priceFormatter),
                    quantity = it.quantity,
                )
            },
            priceFactory = priceFactory,
        )
    }
}