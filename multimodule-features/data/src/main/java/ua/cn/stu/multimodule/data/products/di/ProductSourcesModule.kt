package ua.cn.stu.multimodule.data.products.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.products.sources.DiscountsDataSource
import ua.cn.stu.multimodule.data.products.sources.InMemoryDiscountsDataSource
import ua.cn.stu.multimodule.data.products.sources.InMemoryProductsDataSource
import ua.cn.stu.multimodule.data.products.sources.ProductsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProductSourcesModule {

    @Binds
    @Singleton
    fun bindProductsSource(
        productsDataSource: InMemoryProductsDataSource
    ): ProductsDataSource

    @Binds
    @Singleton
    fun bindDiscountsSource(
        discountsDataSource: InMemoryDiscountsDataSource
    ): DiscountsDataSource

}