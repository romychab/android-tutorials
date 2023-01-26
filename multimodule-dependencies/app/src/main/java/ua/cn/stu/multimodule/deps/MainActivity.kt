package ua.cn.stu.multimodule.deps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import ua.cn.stu.multimodule.deps.androidlib.HelloWorld
import ua.cn.stu.multimodule.deps.androidlib.TestJob
import ua.cn.stu.multimodule.uialerts.UiAlerts

/**
 * Dependency examples:
 *
 * # 1. 'implementation' example
 *
 * ```
 *   +------+
 *   | :app |
 *   +------+
 *    |
 *    |  implementation project(':my-android-library')
 *    V
 *  +---------------------+
 *  | :my-android-library |
 *  +---------------------+
 *    |
 *    | implementation project(':my-kotlin-library')
 *    V
 *  +--------------------+
 *  | :my-kotlin-library |
 *  +--------------------+
 * ```
 *
 * Results:
 * - :app module depends only on :my-android-library and doesn't depend on :my-kotlin-library.
 * - :app module sees classes/methods located in :my-android-library
 * - :app module doesn't see classes/methods located in :my-kotlin-library
 * - :my-android-library sees classes/methods located in :my-kotlin-library
 *
 *
 * # 2. 'api' example
 *
 * ```
 *   +------+
 *   | :app |
 *   +------+
 *    |
 *    |  implementation project(':my-android-library')
 *    V
 *  +---------------------+
 *  | :my-android-library |
 *  +---------------------+
 *    |
 *    | api project(':my-kotlin-library')
 *    V
 *  +--------------------+
 *  | :my-kotlin-library |
 *  +--------------------+
 * ```
 *
 * Results:
 * - :app module depends both on :my-android-library and :my-kotlin-library.
 * - :app module sees classes/methods located in :my-android-library and :my-kotlin-library
 * - :my-android-library sees classes/methods located in :my-kotlin-library
 *
 *
 * # 3. 'compileOnly' example
 *
 * ```
 *   +------+             implementation 'io.reactivex...'  # OPTIONAL
 *   | :app |----------------------------------------------------+
 *   +------+                                                    |
 *    |                                                          |      +---------+
 *    |  implementation project(':my-android-library')           +----->| Rx Java |
 *    V                                                          |      +---------+
 *  +---------------------+  compileOnly 'io.reactivex...'       |
 *  | :my-android-library |--------------------------------------+
 *  +---------------------+
 *
 * ```
 *
 * Results:
 * - :app depends on RxJava directly so it can use safely Rx methods from :my-android-library
 * - :my-android-library can use RxJava
 * - RxJava library WILL NOT BE included to the final build IF you don't add RxJava as a dependency
 *   to the app/build.gradle. In this case you CAN use [TestJob.doJob] method in the :app module
 *   but you CAN'T use [TestJob.doJobRx] method.
 * - RxJava library WILL BE included to the final build IF you add RxJava as a dependency to
 *   the app/build.gradle: `implementation "io.reactivex.rxjava3:rxjava:3.1.6"`. In this case you
 *   can call both [TestJob.doJob] and [TestJob.doJobRx] methods.
 *
 *
 * # 4. 'runtimeOnly' example
 *
 * ```
 *   +------+             implementation project(':ui-alerts')         +-----------------+
 *   | :app |----------------------------------------------------+---->| :ui-alerts-impl |
 *   +------+             runtimeOnly project(':ui-alerts-impl') |     +-----------------+
 *    |                                                          |              |
 *    |                                                          |              V  implementation project(':ui-alerts')
 *    |  implementation project(':my-android-library')           |       +------+-----+
 *    |                                                          +------>| :ui-alerts |
 *    |                                                                  +------------+
 *    V                                                                       ^
 *  +---------------------+  implementation project(':ui-alerts')             |
 *  | :my-android-library |---------------------------------------------------+
 *  +---------------------+
 *
 * ```
 *
 * Results:
 * - making changes in the :ui-alerts-impl module doesn't lead to recompilation of other modules at all
 * - you can create a lot of modules which depend on :ui-alerts.
 * - implementation is added only to the :app module by using 'runtimeOnly'
 * - you can swap/replace/change the real implementation of :ui-alerts module at any time without rebuilding
 *   other modules.
 *
 * */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UiAlerts.init(applicationContext)

        setContentView(R.layout.activity_main)

        HelloWorld.sayHello()

        setupDoJobButtons()
    }

    private fun setupDoJobButtons() {
        val testJob = TestJob()

        findViewById<Button>(R.id.doJobButton).setOnClickListener {

            UiAlerts.toast("app module: job started")

            testJob.doJob {
                Log.d("MainActivity", "Job Finished")
            }
        }

        findViewById<Button>(R.id.doJobButtonRx).setOnClickListener {

            UiAlerts.toast("app module: job started")

            testJob.doJobRx().subscribe {
                Log.d("MainActivity", "Job Finished")
            }

        }
    }
}