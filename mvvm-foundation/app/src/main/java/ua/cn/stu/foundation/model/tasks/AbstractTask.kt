package ua.cn.stu.foundation.model.tasks

import ua.cn.stu.foundation.model.ErrorResult
import ua.cn.stu.foundation.model.FinalResult
import ua.cn.stu.foundation.model.SuccessResult
import ua.cn.stu.foundation.model.tasks.dispatchers.Dispatcher
import ua.cn.stu.foundation.model.tasks.factories.TaskBody
import ua.cn.stu.foundation.utils.delegates.Await

/**
 * Base class for easier creation of new tasks.
 * Provides 2 methods which should be implemented: [doEnqueue] and [doCancel]
 */
abstract class AbstractTask<T> : Task<T> {

    private var finalResult by Await<FinalResult<T>>()

    final override fun await(): T {
        val wrapperListener: TaskListener<T> = {
            finalResult = it
        }
        doEnqueue(wrapperListener)
        try {
            when (val result = finalResult) {
                is ErrorResult -> throw result.exception
                is SuccessResult -> return result.data
            }
        } catch (e: Exception) {
            if (e is InterruptedException) {
                cancel()
                throw CancelledException(e)
            } else {
                throw e
            }
        }
    }

    final override fun enqueue(dispatcher: Dispatcher, listener: TaskListener<T>) {
        val wrappedListener: TaskListener<T> = {
            finalResult = it
            dispatcher.dispatch {
                listener(finalResult)
            }
        }
        doEnqueue(wrappedListener)
    }

    final override fun cancel() {
        finalResult = ErrorResult(CancelledException())
        doCancel()
    }

    fun executeBody(taskBody: TaskBody<T>, listener: TaskListener<T>) {
        try {
            val data = taskBody()
            listener(SuccessResult(data))
        } catch (e: Exception) {
            listener(ErrorResult(e))
        }
    }

    /**
     * Launch the task asynchronously. Listener should be called when task is finished.
     * You may also use [executeBody] if your task executes [TaskBody] in some way.
     */
    abstract fun doEnqueue(listener: TaskListener<T>)

    /**
     * Cancel the task.
     */
    abstract fun doCancel()

}