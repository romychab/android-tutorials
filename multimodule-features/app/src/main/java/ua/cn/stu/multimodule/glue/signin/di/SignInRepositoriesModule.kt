package ua.cn.stu.multimodule.glue.signin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.signin.repositories.AdapterAuthServiceRepository
import ua.cn.stu.multimodule.glue.signin.repositories.AdapterAuthTokenRepository
import ua.cn.stu.multimodule.glue.signin.repositories.AdapterProfileRepository
import ua.cn.stu.multimodule.signin.domain.repositories.AuthServiceRepository
import ua.cn.stu.multimodule.signin.domain.repositories.AuthTokenRepository
import ua.cn.stu.multimodule.signin.domain.repositories.ProfileRepository

@Module
@InstallIn(SingletonComponent::class)
interface SignInRepositoriesModule {

    @Binds
    fun bindAuthRepository(
        authServiceRepository: AdapterAuthServiceRepository
    ): AuthServiceRepository

    @Binds
    fun bindAuthTokenRepository(
        authTokenRepository: AdapterAuthTokenRepository
    ): AuthTokenRepository

    @Binds
    fun bindProfileRepository(
        profileRepository: AdapterProfileRepository
    ): ProfileRepository

}