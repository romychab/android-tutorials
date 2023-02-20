package ua.cn.stu.multimodule.glue.orders.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.orders.repositories.AdapterCartRepository
import ua.cn.stu.multimodule.glue.orders.repositories.AdapterOrdersRepository
import ua.cn.stu.multimodule.glue.orders.repositories.AdapterProductsRepository
import ua.cn.stu.multimodule.orders.domain.repositories.CartRepository
import ua.cn.stu.multimodule.orders.domain.repositories.OrdersRepository
import ua.cn.stu.multimodule.orders.domain.repositories.ProductsRepository

@Module
@InstallIn(SingletonComponent::class)
interface OrderRepositoriesModule {

    @Binds
    fun bindOrdersRepository(
        ordersRepository: AdapterOrdersRepository
    ): OrdersRepository

    @Binds
    fun bindCartRepository(
        cartRepository: AdapterCartRepository
    ): CartRepository

    @Binds
    fun bindProductsRepository(
        productsRepository: AdapterProductsRepository
    ): ProductsRepository

}