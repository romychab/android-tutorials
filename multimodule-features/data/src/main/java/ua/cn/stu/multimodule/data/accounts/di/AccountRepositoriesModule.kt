package ua.cn.stu.multimodule.data.accounts.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.data.accounts.RealAccountsDataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AccountRepositoriesModule {

    @Binds
    @Singleton
    fun bindAccountsRepository(
        accountsDataRepository: RealAccountsDataRepository
    ): AccountsDataRepository

}