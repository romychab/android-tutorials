package ua.cn.stu.robolectric.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.robolectric.model.CatsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoriesModule {

    @Provides
    @Singleton
    fun provideCatsRepository(): CatsRepository = CatsRepository()

}