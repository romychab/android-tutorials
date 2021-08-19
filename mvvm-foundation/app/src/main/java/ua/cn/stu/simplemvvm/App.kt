package ua.cn.stu.simplemvvm

import android.app.Application
import ua.cn.stu.foundation.BaseApplication
import ua.cn.stu.foundation.model.tasks.ThreadUtils
import ua.cn.stu.foundation.model.tasks.dispatchers.MainThreadDispatcher
import ua.cn.stu.foundation.model.tasks.factories.ExecutorServiceTasksFactory
import ua.cn.stu.foundation.model.tasks.factories.HandlerThreadTasksFactory
import ua.cn.stu.simplemvvm.model.colors.InMemoryColorsRepository
import java.util.concurrent.Executors

/**
 * Here we store instances of model layer classes.
 */
class App : Application(), BaseApplication {

    // instances of all created task factories
    private val singleThreadExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newSingleThreadExecutor())
    private val handlerThreadTasksFactory = HandlerThreadTasksFactory()
    private val cachedThreadPoolExecutorTasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    /**
     * Place your singleton scope dependencies here
     */
    override val singletonScopeDependencies: List<Any> = listOf(
        cachedThreadPoolExecutorTasksFactory, // task factory to be used in view-models
        dispatcher, // dispatcher to be used in view-models

        InMemoryColorsRepository(cachedThreadPoolExecutorTasksFactory, threadUtils)
    )

}