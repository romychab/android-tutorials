package ua.cn.stu.foundation.model.tasks

import ua.cn.stu.foundation.model.FinalResult
import ua.cn.stu.foundation.model.tasks.dispatchers.Dispatcher
import ua.cn.stu.foundation.model.tasks.dispatchers.MainThreadDispatcher

typealias TaskListener<T> = (FinalResult<T>) -> Unit

class CancelledException(
    originException: Exception? = null
) : Exception(originException)

/**
 * Base interface for all async operations.
 */
interface Task<T> {

    /**
     * Blocking method for waiting and getting results.
     * Throws exception in case of error.
     * Task may be executed only once.
     * @throws [IllegalStateException] if task has been already executed
     * @throws [CancelledException] if task has been cancelled
     */
    fun await(): T

    /**
     * Non-blocking method for listening task results.
     * If task is cancelled before finishing, listener is not called.
     * If task is cancelled before calling this method, task is not executed.
     * Task may be executed only once.
     *
     * Listener is called via the specified dispatcher. Usually it is [MainThreadDispatcher]
     * @throws [IllegalStateException] if task has been already executed.
     */
    fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>)

    /**
     * Cancel this task and remove listener assigned by [enqueue].
     */
    fun cancel()

}