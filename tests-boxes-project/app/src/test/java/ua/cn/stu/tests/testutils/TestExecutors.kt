package ua.cn.stu.tests.testutils

import io.mockk.every
import io.mockk.mockk
import java.util.concurrent.*

/**
 * Mocked [ExecutorService] which launches commands immediately and
 * returns mocked [Future] when calling [ExecutorService.submit] method.
 */
fun immediateExecutorService(): ExecutorService {
    val service = mockk<ExecutorService>()
    every { service.execute(any()) } answers {
        firstArg<Runnable>().run()
    }
    every { service.submit(any()) } answers {
        firstArg<Runnable>().run()
        mockk(relaxed = true)
    }
    return service
}
