package ua.cn.stu.tests.utils.async

import java.util.concurrent.ExecutorService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLazyListenersFactory @Inject constructor() : LazyListenersFactory {

    override fun <A : Any, T : Any> createLazyListenersSubject(
        loaderExecutor: ExecutorService,
        handlerExecutor: ExecutorService,
        loader: ValueLoader<A, T>
    ): LazyListenersSubject<A, T> {
        return LazyListenersSubject(
            loaderExecutor = loaderExecutor,
            handlerExecutor = handlerExecutor,
            loader = loader
        )
    }
}