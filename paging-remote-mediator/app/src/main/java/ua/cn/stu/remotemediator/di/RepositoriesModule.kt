package ua.cn.stu.remotemediator.di

import androidx.paging.ExperimentalPagingApi
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.remotemediator.data.DefaultLaunchesRepository
import ua.cn.stu.remotemediator.domain.LaunchesRepository

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun bindLaunchesRepository(
        launchesRepository: DefaultLaunchesRepository
    ): LaunchesRepository

}