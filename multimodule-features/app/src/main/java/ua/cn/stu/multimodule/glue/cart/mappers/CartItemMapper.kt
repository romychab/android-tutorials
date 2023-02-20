package ua.cn.stu.multimodule.glue.cart.mappers

import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.entities.Product
import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.data.cart.entities.CartItemDataEntity
import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.cart.entities.CartUsdPrice
import javax.inject.Inject

class CartItemMapper @Inject constructor(
    private val productsDataRepository: ProductsDataRepository,
    private val priceFormatter: PriceFormatter,
) {

    suspend fun toCartItem(dataEntity: CartItemDataEntity): CartItem {
        val productDataEntity = productsDataRepository.getProductById(dataEntity.id)
        val productPriceWithDiscount = productsDataRepository.getDiscountPriceUsdCentsForEntity(productDataEntity)
        val product = Product(
            id = productDataEntity.id,
            name = productDataEntity.name,
            shortDetails = productDataEntity.shortDescription,
            photo = productDataEntity.imageUrl,
            totalQuantity = productDataEntity.quantityAvailable
        )
        val discountPerItem = productDataEntity.priceUsdCents - (productPriceWithDiscount ?: productDataEntity.priceUsdCents)
        return CartItem(
            id = dataEntity.id,
            product = product,
            quantity = dataEntity.quantity,
            pricePerItem = CartUsdPrice(productDataEntity.priceUsdCents, priceFormatter),
            discountPerItem = CartUsdPrice(discountPerItem, priceFormatter)
        )
    }
}