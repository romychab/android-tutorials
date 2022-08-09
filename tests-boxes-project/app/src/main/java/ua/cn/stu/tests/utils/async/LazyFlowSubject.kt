package ua.cn.stu.tests.utils.async

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import ua.cn.stu.tests.domain.Result

typealias SuspendValueLoader<A, T> = suspend (A) -> T?

/**
 * The same as [LazyListenersSubject] but adapted for using with kotlin flows.
 * @see LazyListenersSubject
 */
class LazyFlowSubject<A : Any, T : Any>(
    lazyListenersFactory: LazyListenersFactory,
    private val loader: SuspendValueLoader<A, T>
) {

    private val lazyListenersSubject =
        lazyListenersFactory.createLazyListenersSubject<A, T> { arg ->
            runBlocking {
                loader.invoke(arg)
            }
        }

    /**
     * @see [LazyListenersSubject.reloadAll]
     */
    fun reloadAll(silentMode: Boolean = false) {
        lazyListenersSubject.reloadAll(silentMode)
    }

    /**
     * @see LazyListenersSubject.reloadArgument
     */
    fun reloadArgument(argument: A, silentMode: Boolean = false) {
        lazyListenersSubject.reloadArgument(argument, silentMode)
    }

    /**
     * @see LazyListenersSubject.updateAllValues
     */
    fun updateAllValues(newValue: T?) {
        lazyListenersSubject.updateAllValues(newValue)
    }

    /**
     * @see LazyListenersSubject.addListener
     * @see LazyListenersSubject.removeListener
     */
    fun listen(argument: A): Flow<Result<T>> = callbackFlow {
        val listener: ValueListener<T> = { result ->
            trySend(result)
        }
        lazyListenersSubject.addListener(argument, listener)
        awaitClose {
            lazyListenersSubject.removeListener(argument, listener)
        }
    }

}