package ua.cn.stu.foundation.model.tasks.factories

import ua.cn.stu.foundation.model.tasks.AbstractTask
import ua.cn.stu.foundation.model.tasks.SynchronizedTask
import ua.cn.stu.foundation.model.tasks.Task
import ua.cn.stu.foundation.model.tasks.TaskListener

/**
 * Factory that creates tasks which are launched in a separate thread:
 * one thread per each task. Threads are created by using [Thread] class.
 */
class ThreadTasksFactory : TasksFactory {

    override fun <T> async(body: TaskBody<T>): Task<T> {
        return SynchronizedTask(ThreadTask(body))
    }

    private class ThreadTask<T>(
        private val body: TaskBody<T>
    ) : AbstractTask<T>() {

        private var thread: Thread? = null

        override fun doEnqueue(listener: TaskListener<T>) {
            thread = Thread {
                executeBody(body, listener)
            }
            thread?.start()
        }

        override fun doCancel() {
            thread?.interrupt()
        }

    }

}