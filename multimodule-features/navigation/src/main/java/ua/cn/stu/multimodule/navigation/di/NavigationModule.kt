package ua.cn.stu.multimodule.navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import ua.cn.stu.multimodule.core.AppRestarter
import ua.cn.stu.multimodule.core.impl.ActivityRequired
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import ua.cn.stu.multimodule.navigation.MainAppRestarter

@Module
@InstallIn(SingletonComponent::class)
class NavigationModule {

    @Provides
    fun provideAppRestarter(
        appRestarter: MainAppRestarter
    ): AppRestarter {
        return appRestarter
    }

    @Provides
    @IntoSet
    fun provideRouterAsActivityRequired(
        router: GlobalNavComponentRouter,
    ): ActivityRequired {
        return router
    }


}