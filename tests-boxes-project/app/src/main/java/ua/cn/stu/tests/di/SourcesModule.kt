package ua.cn.stu.tests.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.tests.domain.accounts.AccountsSource
import ua.cn.stu.tests.domain.boxes.BoxesSource
import ua.cn.stu.tests.data.accounts.RetrofitAccountsSource
import ua.cn.stu.tests.data.boxes.RetrofitBoxesSource

/**
 * This module binds concrete sources implementations to their
 * interfaces: [RetrofitAccountsSource] is bound to [AccountsSource]
 * and [RetrofitBoxesSource] is bound to [BoxesSource].
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    @Binds
    abstract fun bindAccountsSource(
        retrofitAccountsSource: RetrofitAccountsSource
    ): AccountsSource

    @Binds
    abstract fun bindBoxesSource(
        retrofitBoxesSource: RetrofitBoxesSource
    ): BoxesSource

}