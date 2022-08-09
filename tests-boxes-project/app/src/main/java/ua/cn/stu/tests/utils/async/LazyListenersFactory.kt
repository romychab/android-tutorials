package ua.cn.stu.tests.utils.async

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

interface LazyListenersFactory {

    /**
     * Create an instance of [LazyListenersSubject]
     */
    fun <A : Any, T : Any> createLazyListenersSubject(
        // for real server it's better to use cached thread pool.
        loaderExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
        // single thread pool to avoid multi-threading issues
        handlerExecutor: ExecutorService = Executors.newSingleThreadExecutor(),
        loader: ValueLoader<A, T>
    ): LazyListenersSubject<A, T>

}