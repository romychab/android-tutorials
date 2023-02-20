package ua.cn.stu.multimodule.glue.cart.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.repositories.CartRepository
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.CartDataRepository
import ua.cn.stu.multimodule.glue.cart.mappers.CartItemMapper
import javax.inject.Inject

class AdapterCartRepository @Inject constructor(
    private val cartDataRepository: CartDataRepository,
    private val cartItemMapper: CartItemMapper,
) : CartRepository {

    override suspend fun changeQuantity(cartItemId: Long, newQuantity: Int) {
        cartDataRepository.changeQuantity(cartItemId, newQuantity)
    }

    override suspend fun deleteCartItems(cartItemIds: List<Long>) {
        cartDataRepository.deleteCartItem(cartItemIds)
    }

    override suspend fun getCartItemById(id: Long): CartItem {
        return cartItemMapper.toCartItem(cartDataRepository.getCartItemById(id))
    }

    override fun getCartItems(): Flow<Container<List<CartItem>>> {
        return cartDataRepository.getCart().map { container ->
            container.suspendMap { list ->
                list.map { cartItemMapper.toCartItem(it) }
            }
        }
    }

    override fun reload() {
        cartDataRepository.reload()
    }
}