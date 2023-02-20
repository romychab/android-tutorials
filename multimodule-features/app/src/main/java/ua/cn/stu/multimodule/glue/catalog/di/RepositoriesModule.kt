package ua.cn.stu.multimodule.glue.catalog.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.catalog.domain.repositories.CartRepository
import ua.cn.stu.multimodule.catalog.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.glue.catalog.repositories.AdapterCartRepository
import ua.cn.stu.multimodule.glue.catalog.repositories.AdapterProductsRepository

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun provideProductsRepository(
        repository: AdapterProductsRepository
    ): ProductsRepository

    @Binds
    fun provideCartRepository(
        repository: AdapterCartRepository
    ): CartRepository

}