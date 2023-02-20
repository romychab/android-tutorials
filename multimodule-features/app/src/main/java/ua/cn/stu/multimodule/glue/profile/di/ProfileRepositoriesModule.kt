package ua.cn.stu.multimodule.glue.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.profile.repositories.AdapterAuthTokenRepository
import ua.cn.stu.multimodule.glue.profile.repositories.AdapterProfileRepository
import ua.cn.stu.multimodule.profile.domain.repositories.AuthTokenRepository
import ua.cn.stu.multimodule.profile.domain.repositories.ProfileRepository

@Module
@InstallIn(SingletonComponent::class)
interface ProfileRepositoriesModule {

    @Binds
    fun bindAuthTokenRepository(
        authTokenRepository: AdapterAuthTokenRepository
    ): AuthTokenRepository

    @Binds
    fun bindProfileRepository(
        profileRepository: AdapterProfileRepository
    ): ProfileRepository

}