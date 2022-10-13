package ua.cn.stu.espresso.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.espresso.model.CatsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideCatsRepository(): CatsRepository = CatsRepository()

}