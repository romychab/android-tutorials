package ua.cn.stu.multimodule.navigation.domain.repositories

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container

interface GetCartItemCountRepository {

    /**
     * Listen for the count of items added to the cart.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getCartItemCount(): Flow<Container<Int>>

}