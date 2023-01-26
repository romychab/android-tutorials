package ua.cn.stu.multimodule.deps.androidlib

import io.reactivex.rxjava3.core.Completable
import ua.cn.stu.multimodule.uialerts.UiAlerts
import java.util.concurrent.Executors

class TestJob {

    private val executorService = Executors.newSingleThreadExecutor()

    /**
     * Wait 2 sec and call onSuccess callback
     */
    fun doJob(onSuccess: () -> Unit) {
        executorService.execute {
            // pretend we are doing a heavy job
            Thread.sleep(2000)
            // all done
            UiAlerts.toast("my-android-library: job finished")
            onSuccess()
        }
    }

    /**
     * The same as [doJob] but for Rx lovers.
     */
    fun doJobRx(): Completable {
        return Completable.create { emitter ->
            doJob {
                emitter.onComplete()
            }
        }
    }

}