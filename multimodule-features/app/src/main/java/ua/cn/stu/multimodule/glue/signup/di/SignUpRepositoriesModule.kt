package ua.cn.stu.multimodule.glue.signup.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.glue.signup.repositories.AdapterSignUpRepository
import ua.cn.stu.multimodule.signup.domain.repositories.SignUpRepository

@Module
@InstallIn(SingletonComponent::class)
interface SignUpRepositoriesModule {

    @Binds
    fun bindSignUpDataSource(signUpDataSource: AdapterSignUpRepository): SignUpRepository

}