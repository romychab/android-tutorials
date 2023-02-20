package ua.cn.stu.multimodule.catalog.domain

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import ua.cn.stu.multimodule.catalog.domain.entities.Product
import ua.cn.stu.multimodule.catalog.domain.repositories.CartRepository
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.Core
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
) {

    /**
     * Add a new product to the Cart.
     */
    suspend fun addToCart(product: Product) = withTimeout(Core.localTimeoutMillis) {
        val productIdsInCart = cartRepository.getProductIdentifiersInCart()
            .filterIsInstance<Container.Success<Set<Long>>>()
            .first()
        if (!productIdsInCart.value.contains(product.id)) {
            cartRepository.addToCart(product.id)
        }
    }
}