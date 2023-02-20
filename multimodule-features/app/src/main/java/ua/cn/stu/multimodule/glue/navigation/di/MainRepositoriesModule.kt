package ua.cn.stu.multimodule.glue.navigation.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.navigation.repositories.AdapterGetCartItemCountRepository
import ua.cn.stu.multimodule.glue.navigation.repositories.AdapterGetCurrentUsernameRepository
import ua.cn.stu.multimodule.navigation.domain.repositories.GetCartItemCountRepository
import ua.cn.stu.multimodule.navigation.domain.repositories.GetCurrentUsernameRepository

@Module
@InstallIn(SingletonComponent::class)
interface MainRepositoriesModule {

    @Binds
    fun bindGetCurrentUsernameRepository(
        getCurrentUsernameRepository: AdapterGetCurrentUsernameRepository
    ): GetCurrentUsernameRepository

    @Binds
    fun bindGetCartItemCountRepository(
        getCartItemCountRepository: AdapterGetCartItemCountRepository
    ): GetCartItemCountRepository

}