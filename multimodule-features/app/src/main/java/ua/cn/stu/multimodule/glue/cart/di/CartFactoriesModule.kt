package ua.cn.stu.multimodule.glue.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.cart.domain.factories.PriceFactory
import ua.cn.stu.multimodule.glue.cart.factories.DefaultCartPriceFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface CartFactoriesModule {

    @Binds
    @Singleton
    fun bindPriceFactory(priceFactory: DefaultCartPriceFactory): PriceFactory

}