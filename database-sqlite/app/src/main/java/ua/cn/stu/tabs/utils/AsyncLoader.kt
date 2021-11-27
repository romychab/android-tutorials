package ua.cn.stu.tabs.utils

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Load a value only once and cache it.
 * @param loader performs loading a value (shouldn't throw exceptions)
 */
class AsyncLoader<T>(
    private val loader: suspend () -> T,
) {

    // Implementation example by using Mutex.
    // async() and Deferred<T> can be used instead too.

    private val mutex = Mutex()
    private var value: T? = null

    suspend fun get(): T {
        mutex.withLock {
            if (value == null) {
                value = loader()
            }
        }
        return value!!
    }

}