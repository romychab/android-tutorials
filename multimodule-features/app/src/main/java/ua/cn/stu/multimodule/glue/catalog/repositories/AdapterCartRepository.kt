package ua.cn.stu.multimodule.glue.catalog.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.catalog.domain.repositories.CartRepository
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.CartDataRepository
import javax.inject.Inject

class AdapterCartRepository @Inject constructor(
    private val cartDataRepository: CartDataRepository,
) : CartRepository {

    override fun getProductIdentifiersInCart(): Flow<Container<Set<Long>>> {
        return cartDataRepository.getCart().map { container ->
            container.map { list ->
                list.map { it.productId }.toSet()
            }
        }
    }

    override fun reload() {
        cartDataRepository.reload()
    }

    override suspend fun addToCart(productId: Long) {
        cartDataRepository.addToCart(productId, quantity = 1)
    }

}