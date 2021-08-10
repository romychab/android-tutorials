package ua.cn.stu.foundation.model.tasks

import ua.cn.stu.foundation.model.FinalResult

typealias TaskListener<T> = (FinalResult<T>) -> Unit

/**
 * Base interface for all async operations.
 */
interface Task<T> {

    /**
     * Blocking method for waiting and getting results.
     * Throws exception in case of error.
     * @throws [IllegalStateException] if task has been already executed
     */
    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task is cancelled before finishing, listener is not called.
     *
     * Listener is called in main thread.
     * @throws [IllegalStateException] if task has been already executed.
     */
    fun enqueue(listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue].
     */
    fun cancel()

}