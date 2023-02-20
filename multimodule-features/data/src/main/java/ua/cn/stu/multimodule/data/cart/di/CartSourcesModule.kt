package ua.cn.stu.multimodule.data.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.cart.sources.CartDataSource
import ua.cn.stu.multimodule.data.cart.sources.InMemoryCartDataSource
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
interface CartSourcesModule {

    @Binds
    @Singleton
    fun bindCartSource(cartDataSource: InMemoryCartDataSource): CartDataSource

}