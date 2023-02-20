package ua.cn.stu.multimodule.data.accounts.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.accounts.sources.AccountsDataSource
import ua.cn.stu.multimodule.data.accounts.sources.InMemoryAccountsDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountSourcesModule {

    @Binds
    @Singleton
    fun bindAccountSource(
        accountsDataSource: InMemoryAccountsDataSource
    ): AccountsDataSource

}