package ua.cn.stu.foundation.model.tasks.factories

import ua.cn.stu.foundation.model.tasks.AbstractTask
import ua.cn.stu.foundation.model.tasks.SynchronizedTask
import ua.cn.stu.foundation.model.tasks.Task
import ua.cn.stu.foundation.model.tasks.TaskListener
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * Factory that creates task which are launched by the specified [ExecutorService].
 * For example you may pass [Executors.newCachedThreadPool] in order to use a pool of cached threads
 * or [Executors.newSingleThreadExecutor] for launching tasks one by one.
 */
class ExecutorServiceTasksFactory(
    private val executorService: ExecutorService
) : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ExecutorServiceTask(body))
    }

    private inner class ExecutorServiceTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var future: Future<*>? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            future = executorService.submit {
                executeBody(body, listener)
            }
        }

        override fun doCancel() {
            future?.cancel(true)
        }
    }
}