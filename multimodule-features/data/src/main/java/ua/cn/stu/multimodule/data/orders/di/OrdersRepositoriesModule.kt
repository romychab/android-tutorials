package ua.cn.stu.multimodule.data.orders.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.OrdersDataRepository
import ua.cn.stu.multimodule.data.orders.RealOrdersDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface OrdersRepositoriesModule {

    @Binds
    @Singleton
    fun bindOrdersRepository(
        ordersDataRepository: RealOrdersDataRepository
    ): OrdersDataRepository

}