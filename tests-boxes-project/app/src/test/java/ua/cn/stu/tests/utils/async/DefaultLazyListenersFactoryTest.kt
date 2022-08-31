package ua.cn.stu.tests.utils.async

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.testutils.immediateExecutorService

class DefaultLazyListenersFactoryTest {

    @Test
    fun createLazyListenersSubject() {
        val factory = DefaultLazyListenersFactory()
        val loader: ValueLoader<String, String> = mockk()
        val listener: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg") } returns "result"

        val subject: LazyListenersSubject<String, String> =
            factory.createLazyListenersSubject(
                loaderExecutor = immediateExecutorService(),
                handlerExecutor = immediateExecutorService(),
                loader = loader
            )
        subject.addListener("arg", listener)

        verify {
            listener(Success("result"))
        }
    }

}