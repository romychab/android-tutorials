package ua.cn.stu.multimodule

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ua.cn.stu.multimodule.core.Core
import ua.cn.stu.multimodule.core.CoreProvider
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var coreProvider: CoreProvider

    override fun onCreate() {
        super.onCreate()
        Core.init(coreProvider)
    }

}