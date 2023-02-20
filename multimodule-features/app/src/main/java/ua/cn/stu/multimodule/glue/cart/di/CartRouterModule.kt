package ua.cn.stu.multimodule.glue.cart.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.cart.presentation.CartRouter
import ua.cn.stu.multimodule.glue.cart.AdapterCartRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface CartRouterModule {

    @Binds
    fun bindCartRouter(cartRouter: AdapterCartRouter): CartRouter

}