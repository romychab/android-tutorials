package ua.cn.stu.simplemvvm

import android.app.Application
import ua.cn.stu.foundation.BaseApplication
import ua.cn.stu.foundation.model.coroutines.IoDispatcher
import ua.cn.stu.foundation.model.coroutines.WorkerDispatcher
import ua.cn.stu.simplemvvm.model.colors.InMemoryColorsRepository

/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    // holder classes are used because we have 2 dispatchers of the same type
    private val ioDispatcher = IoDispatcher() // for IO operations
    private val workerDispatcher = WorkerDispatcher() // for CPU-intensive operations

    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        ioDispatcher,
        workerDispatcher,

        InMemoryColorsRepository(ioDispatcher)
    )

}