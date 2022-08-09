package ua.cn.stu.tests.utils.async

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultLazyFlowFactory @Inject constructor(
    private val lazyListenersFactory: LazyListenersFactory
) : LazyFlowFactory {

    override fun <A : Any, T : Any> createLazyFlowSubject(
        loader: SuspendValueLoader<A, T>
    ): LazyFlowSubject<A, T> {
        return LazyFlowSubject(lazyListenersFactory, loader)
    }

}