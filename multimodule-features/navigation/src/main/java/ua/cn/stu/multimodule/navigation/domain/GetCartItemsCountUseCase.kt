package ua.cn.stu.multimodule.navigation.domain

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.navigation.domain.repositories.GetCartItemCountRepository
import javax.inject.Inject

class GetCartItemsCountUseCase @Inject constructor(
    private val getCartItemCountRepository: GetCartItemCountRepository,
){

    /**
     * Listen for the count of items added to the cart.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getCartItemCount(): Flow<Container<Int>> {
        return getCartItemCountRepository.getCartItemCount()
    }

}