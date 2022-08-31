package ua.cn.stu.tests.testutils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest


/**
 * Helper method for testing infinite Kotlin Flows.
 * Usage example:
 * ```
 *   @Test
 *   fun testFlow() = runFlowTest {
 *     val repository = UsersRepository()
 *
 *     // assuming getCurrentUserFlow returns infinite flow which
 *     // notifies about the current user
 *     val flow = repository.getCurrentUserFlow()
 *
 *     val collectedUsers = flow.startCollecting()
 *     // changing the current user 3 times
 *     repository.setUser(User(id = 1))
 *     repository.setUser(User(id = 2))
 *     repository.setUser(User(id = 3))
 *
 *     // all flow values are recorded to the collectedUsers list
 *     assertEquals(
 *       listOf(User(id = 1), User(id = 2), User(id = 3)),
 *       collectedUsers
 *     )
 * }
 * ```
 */
@ExperimentalCoroutinesApi


fun runFlowTest(
    testBody: suspend FlowTest.() -> Unit
) {
    runTest {
        val scope = FlowTest(this)
        testBody.invoke(scope)
        scope.cancelAll()
    }
}

@ExperimentalCoroutinesApi
class FlowTest(
    private val scope: TestScope
) {

    private val allJobs =
        mutableMapOf<Flow<*>, MutableList<Job>>()

    fun <T> Flow<T>.startCollecting(): List<T> {
        val list = mutableListOf<T>()
        val job = scope.launch(UnconfinedTestDispatcher()) {
            toList(list)
        }
        val jobs = allJobs[this] ?: mutableListOf<Job>().also {
            allJobs[this] = it
        }
        jobs.add(job)

        return list
    }

    fun <T> Flow<T>.cancelCollecting() {
        allJobs[this]?.forEach { it.cancel() }
        allJobs[this]?.clear()
    }

    fun cancelAll() {
        allJobs.forEach {
            it.key.cancelCollecting()
        }
    }
}

