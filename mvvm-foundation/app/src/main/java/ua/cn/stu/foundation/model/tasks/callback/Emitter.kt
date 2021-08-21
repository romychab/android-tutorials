package ua.cn.stu.foundation.model.tasks.callback

import ua.cn.stu.foundation.model.FinalResult
import ua.cn.stu.foundation.model.tasks.Task

typealias CancelListener = () -> Unit

typealias ExecutionListener<T> = (Emitter<T>) -> Unit

/**
 * Emitter instance is passed to [CallbackTask.create] as an argument so you can use it
 * for converting callbacks into [Task].
 */
interface Emitter<T> {

    /**
     * Finish the associated task with the specified result.
     */
    fun emit(finalResult: FinalResult<T>)

    /**
     * Assign optional cancel listener. This listener is executed when the associated task
     * has been cancelled by [Task.cancel] call.
     */
    fun setCancelListener(cancelListener: CancelListener)

    companion object {
        /**
         * Wrap the emitter with some [onFinish] action which will be executed upon
         * publishing result or cancelling. May be useful for cleanup logic.
         */
        fun <T> wrap(emitter: Emitter<T>, onFinish: () -> Unit): Emitter<T> {
            return object : Emitter<T> {
                override fun emit(finalResult: FinalResult<T>) {
                    onFinish()
                    emitter.emit(finalResult)
                }

                override fun setCancelListener(cancelListener: CancelListener) {
                    emitter.setCancelListener {
                        onFinish()
                        cancelListener()
                    }
                }
            }
        }
    }
}