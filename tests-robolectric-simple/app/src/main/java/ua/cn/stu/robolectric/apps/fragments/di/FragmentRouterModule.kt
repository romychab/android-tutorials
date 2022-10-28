package ua.cn.stu.robolectric.apps.fragments.di

import androidx.fragment.app.FragmentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import ua.cn.stu.robolectric.apps.fragments.FragmentRouter

@Module
@InstallIn(ActivityComponent::class)
class FragmentRouterModule {

    @Provides
    fun bindRouter(activity: FragmentActivity): FragmentRouter {
        return activity as FragmentRouter
    }

}