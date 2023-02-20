package ua.cn.stu.multimodule.glue.navigation.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.CartDataRepository
import ua.cn.stu.multimodule.navigation.domain.repositories.GetCartItemCountRepository
import javax.inject.Inject

class AdapterGetCartItemCountRepository @Inject constructor(
    private val cartDataRepository: CartDataRepository,
) : GetCartItemCountRepository {

    override fun getCartItemCount(): Flow<Container<Int>> {
        return cartDataRepository.getCart().map { container ->
            container.map { list -> list.size }
        }
    }

}