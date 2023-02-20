package ua.cn.stu.multimodule.data.products.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.ProductsDataRepository
import ua.cn.stu.multimodule.data.products.RealProductsDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProductRepositoriesModule {

    @Singleton
    @Binds
    fun bindProductRepository(
        productsDataRepository: RealProductsDataRepository
    ): ProductsDataRepository
}