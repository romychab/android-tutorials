package ua.cn.stu.multimodule.core.flow

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container

/**
 * Loader function for [LazyFlowSubject].
 */
typealias ValueLoader<T> = suspend () -> T

/**
 * Async container for loading data and listening the current state
 * of loading process.
 *
 * Value loader is usually specified by [ValueLoader] function passed to
 * the [LazyFlowSubjectFactory.create] as an argument.
 *
 * Loader starts loading value lazily when at least
 * one subscriber starts collecting the flow returned by [listen] method.
 *
 * A value loader can be re-assigned with a new one by using [newLoad] and [newAsyncLoad]
 * methods. In this case an old load is cancelled and replaced by a new one.
 *
 * Also it's possible to update the value directly by calling [updateWith] method.
 */
interface LazyFlowSubject<T> {

    /**
     * Listen for values loaded by this subject.
     * Value is loaded automatically when at least 1 subscriber starts
     * collecting the returned flow.
     */
    fun listen(): Flow<Container<T>>

    /**
     * Start a new load which will replace the existing value in the flow
     * returned by [listen].
     */
    suspend fun newLoad(silently: Boolean = false, valueLoader: ValueLoader<T>? = null): T

    /**
     * The same as [newLoad] but do not wait for the load result.
     */
    fun newAsyncLoad(silently: Boolean = false, valueLoader: ValueLoader<T>? = null)

    /**
     * Update the value immediately in a flow returned by [listen]
     */
    fun updateWith(container: Container<T>)

    /**
     * Update the value immediately in a flow returned by [listen] by
     * using the [updater] function which accepts an old value in arguments.
     */
    fun updateWith(updater: (Container<T>) -> Container<T>)

}