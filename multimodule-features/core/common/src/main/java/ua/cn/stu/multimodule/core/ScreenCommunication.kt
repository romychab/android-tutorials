package ua.cn.stu.multimodule.core

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

typealias ScreenResultListener<T> = (T) -> Unit

interface ScreenCommunication {

    /**
     * Register a listener for the specified screen result [clazz].
     */
    fun <T : Any> registerListener(clazz: Class<T>, listener: ScreenResultListener<T>)

    /**
     * Unregister the specified listener.
     */
    fun <T : Any> unregisterListener(listener: ScreenResultListener<T>)

    /**
     * Send screen result to the first acceptable listener.
     */
    fun <T : Any> publishResult(result: T)

}

/**
 * Extension method for listening screen results by using Kotlin Flow
 * instead of plain callbacks.
 */
fun <T : Any> ScreenCommunication.listen(clazz: Class<T>): Flow<T> = callbackFlow {
    val listener: ScreenResultListener<T> = { result ->
        trySend(result)
    }
    registerListener(clazz, listener)
    awaitClose {
        unregisterListener(listener)
    }
}