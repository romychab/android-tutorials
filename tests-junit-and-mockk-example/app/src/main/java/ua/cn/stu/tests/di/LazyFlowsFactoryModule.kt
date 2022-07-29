package ua.cn.stu.tests.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.cn.stu.tests.utils.async.*

/**
 * This module provides factories for creating:
 * - [LazyListenersSubject]
 * - [LazyFlowSubject]
 */
@Module
@InstallIn(SingletonComponent::class)
interface LazyFlowsFactoryModule {
    @Binds
    fun bindLazyFlowFactory(
        factory: DefaultLazyFlowFactory
    ): LazyFlowFactory

    @Binds
    fun bindLazyListenersFactory(
        factory: DefaultLazyListenersFactory
    ): LazyListenersFactory

}