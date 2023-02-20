package ua.cn.stu.multimodule.glue.signup.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.glue.signup.AdapterSignUpRouter
import ua.cn.stu.multimodule.signup.presentation.SignUpRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface SignUpRouterModule {

    @Binds
    fun bindSignUpRouter(router: AdapterSignUpRouter): SignUpRouter

}