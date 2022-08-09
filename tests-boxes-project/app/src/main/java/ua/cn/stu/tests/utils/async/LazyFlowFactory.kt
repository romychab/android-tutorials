package ua.cn.stu.tests.utils.async

/**
 * Factory interface for creating instances of [LazyFlowSubject].
 */
interface LazyFlowFactory {

    /**
     * Create an instance of [LazyFlowSubject]
     */
    fun <A : Any, T : Any> createLazyFlowSubject(
        loader: SuspendValueLoader<A, T>
    ): LazyFlowSubject<A, T>

}