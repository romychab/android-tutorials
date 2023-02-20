package ua.cn.stu.multimodule.catalog.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.catalog.domain.entities.ProductWithCartInfo
import ua.cn.stu.multimodule.catalog.domain.repositories.CartRepository
import ua.cn.stu.multimodule.catalog.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.core.Container
import javax.inject.Inject

class GetProductDetailsUseCase @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val cartRepository: CartRepository,
) {

    /**
     * Listen for the product info by ID.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getProduct(id: Long): Flow<Container<ProductWithCartInfo>> {
        return cartRepository.getProductIdentifiersInCart()
            .map { container ->
                container.suspendMap { idsInCart ->
                    ProductWithCartInfo(
                        product = productsRepository.getProduct(id),
                        isInCart = idsInCart.contains(id)
                    )
                }
            }
    }

    /**
     * Reload product flow returned by [getProduct]
     */
    fun reload() {
        cartRepository.reload()
    }

}