package ua.cn.stu.tests.utils.async

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.testutils.immediateExecutorService
import ua.cn.stu.tests.testutils.runFlowTest
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class DefaultLazyFlowFactoryTest {

    @Before
    fun setUp() {
        mockkStatic(Executors::class)
        every { Executors.newSingleThreadExecutor() } returns immediateExecutorService()
    }

    @After
    fun tearDown() {
        unmockkStatic(Executors::class)
    }

    @Test
    fun createLazyFlowSubject() = runFlowTest {
        val factory = DefaultLazyFlowFactory(DefaultLazyListenersFactory())
        val loader: SuspendValueLoader<String, String> = mockk()
        coEvery { loader("arg") } returns "result"

        val subject: LazyFlowSubject<String, String> =
            factory.createLazyFlowSubject(loader)
        val collectedResults =
            subject.listen("arg").startCollecting()

        Assert.assertEquals(
            listOf(Pending(), Success("result")),
            collectedResults
        )
    }
}