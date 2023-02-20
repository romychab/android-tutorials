package ua.cn.stu.multimodule.data.orders.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.orders.sources.InMemoryOrdersDataSource
import ua.cn.stu.multimodule.data.orders.sources.OrdersDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface OrdersSourcesModule {

    @Binds
    @Singleton
    fun bindOrdersDataSource(
        ordersDataSource: InMemoryOrdersDataSource,
    ): OrdersDataSource

}