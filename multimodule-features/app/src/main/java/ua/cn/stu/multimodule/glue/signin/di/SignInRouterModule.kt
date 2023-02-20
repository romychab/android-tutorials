package ua.cn.stu.multimodule.glue.signin.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.glue.signin.AdapterSignInRouter
import ua.cn.stu.multimodule.signin.presentation.SignInRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface SignInRouterModule {

    @Binds
    fun bindSignInRouter(router: AdapterSignInRouter): SignInRouter

}