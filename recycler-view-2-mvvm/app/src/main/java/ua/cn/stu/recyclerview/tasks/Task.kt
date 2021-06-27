package ua.cn.stu.recyclerview.tasks

typealias Callback<T> = (T) -> Unit

/**
 * Represents abstract async task which may return result or error.
 */
interface Task<T> {

    /**
     * Register listener which will be called when task finishes with success result
     */
    fun onSuccess(callback: Callback<T>): Task<T>

    /**
     * Register listener which will be called when task finishes with exception
     */
    fun onError(callback: Callback<Throwable>): Task<T>

    /**
     * Cancel task and remove all listeners
     */
    fun cancel()

    /**
     * Block current thread and wait for the results synchronously.
     * Exception is thrown in case of error results.
     */
    fun await(): T

}