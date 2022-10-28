package ua.cn.stu.robolectric.testutils.rules

import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import ua.cn.stu.robolectric.testutils.ImmediateExecutorService
import java.util.concurrent.Executors

/**
 * Remove async operations from RecyclerView & DiffUtil
 */
class ImmediateDiffUtilRule : TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        mockkStatic(Executors::class)
        every { Executors.newFixedThreadPool(any()) } returns ImmediateExecutorService()
    }

    override fun finished(description: Description?) {
        super.finished(description)
        unmockkStatic(Executors::class)
    }
}