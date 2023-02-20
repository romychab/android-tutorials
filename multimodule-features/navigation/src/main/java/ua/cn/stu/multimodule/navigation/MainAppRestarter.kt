package ua.cn.stu.multimodule.navigation

import ua.cn.stu.multimodule.core.AppRestarter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainAppRestarter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : AppRestarter {

    override fun restartApp() {
        globalNavComponentRouter.restart()
    }

}