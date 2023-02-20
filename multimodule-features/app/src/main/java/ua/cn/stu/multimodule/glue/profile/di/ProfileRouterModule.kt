package ua.cn.stu.multimodule.glue.profile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import ua.cn.stu.multimodule.glue.profile.AdapterProfileRouter
import ua.cn.stu.multimodule.profile.presentation.ProfileRouter

@Module
@InstallIn(ActivityRetainedComponent::class)
interface ProfileRouterModule {

    @Binds
    fun bindProfileRouter(
        profileRouter: AdapterProfileRouter,
    ): ProfileRouter

}