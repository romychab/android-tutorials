package ua.cn.stu.multimodule.glue.orders.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.glue.orders.AdapterOrdersRouter
import ua.cn.stu.multimodule.orders.presentation.OrdersRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface RouterModule {

    @Binds
    fun bindRouter(ordersRouter: AdapterOrdersRouter): OrdersRouter

}