package ua.cn.stu.foundation.model.coroutines

import kotlinx.coroutines.CancellableContinuation
import ua.cn.stu.foundation.model.FinalResult

typealias CancelListener = () -> Unit

/**
 * Simpler interface for using instead of [CancellableContinuation]
 */
interface Emitter<T> {

    /**
     * Finish the associated coroutine with the specified result.
     */
    fun emit(finalResult: FinalResult<T>)

    /**
     * Assign optional cancel listener.
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