package ua.cn.stu.multimodule.glue.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.cart.domain.repositories.CartRepository
import ua.cn.stu.multimodule.cart.domain.repositories.ProductsRepository
import ua.cn.stu.multimodule.glue.cart.repositories.AdapterCartRepository
import ua.cn.stu.multimodule.glue.cart.repositories.AdapterProductsRepository

@Module
@InstallIn(SingletonComponent::class)
interface CartRepositoriesModule {

    @Binds
    fun bindCartRepository(cartRepository: AdapterCartRepository): CartRepository

    @Binds
    fun bindProductRepository(productsRepository: AdapterProductsRepository): ProductsRepository

}