package ua.cn.stu.foundation.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * MainThreadExecutor runs [Runnable] instances:
 * - if the current thread is Main Thread -> the [Runnable] is executed immediately
 * - if the current thread is not Main Thread -> the [Runnable] is executed by [Handler] in Main Thread
 */
class MainThreadExecutor : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        if (Looper.getMainLooper().thread.id == Thread.currentThread().id) {
            command.run()
        } else {
            handler.post(command)
        }
    }

}