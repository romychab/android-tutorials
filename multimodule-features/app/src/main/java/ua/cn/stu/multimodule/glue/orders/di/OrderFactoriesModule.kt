package ua.cn.stu.multimodule.glue.orders.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.orders.factories.DefaultOrderPriceFactory
import ua.cn.stu.multimodule.orders.domain.factories.PriceFactory

@Module
@InstallIn(SingletonComponent::class)
interface OrderFactoriesModule {

    @Binds
    fun bindOrderPriceFactory(
        priceFactory: DefaultOrderPriceFactory
    ): PriceFactory

}