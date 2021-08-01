package ua.cn.stu.simplemvvm

import android.app.Application
import ua.cn.stu.foundation.BaseApplication
import ua.cn.stu.foundation.model.Repository
import ua.cn.stu.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    /**
     * Place your repositories here, now we have only 1 repository
     */
    override val repositories: List<Repository> = listOf<Repository>(
        InMemoryColorsRepository()
    )

}