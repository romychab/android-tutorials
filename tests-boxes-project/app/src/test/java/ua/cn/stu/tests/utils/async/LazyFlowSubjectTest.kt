package ua.cn.stu.tests.utils.async

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.testutils.immediateExecutorService
import ua.cn.stu.tests.testutils.runFlowTest
import java.util.concurrent.Executors

@ExperimentalCoroutinesApi
class LazyFlowSubjectTest {

    @get:Rule
    val rule = MockKRule(this)

    @RelaxedMockK
    lateinit var lazyListenersSubject: LazyListenersSubject<String, String>

    @MockK
    lateinit var lazyListenersFactory: LazyListenersFactory

    lateinit var loader: SuspendValueLoader<String, String>

    lateinit var lazyFlowSubject: LazyFlowSubject<String, String>

    @Before
    fun setUp() = runTest {
        loader = mockk(relaxed = true)
        every {
            lazyListenersFactory.createLazyListenersSubject<String, String>(any(), any(), any())
        } returns lazyListenersSubject

        lazyFlowSubject = LazyFlowSubject(lazyListenersFactory, loader)

        mockkStatic(Executors::class)
        every { Executors.newSingleThreadExecutor() } returns immediateExecutorService()
    }

    @After
    fun tearDown() {
        unmockkStatic(Executors::class)
    }

    @Test
    fun initCreatesLazyListenersSubjectWithValidLoader() {
        val slot: CapturingSlot<ValueLoader<String, String>> = slot()
        every {
            lazyListenersFactory.createLazyListenersSubject(
                any(), any(), capture(slot)
            )
        } returns mockk()
        coEvery { loader("arg") } returns "result"

        LazyFlowSubject(lazyListenersFactory, loader)
        val answer = slot.captured("arg")

        assertEquals("result", answer)
    }

    @Test
    fun reloadAllDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.reloadAll(silentMode = true)

        verify(exactly = 1) {
            lazyListenersSubject.reloadAll(silentMode = true)
        }
    }

    @Test
    fun reloadAllDoesNotUseSilentModeByDefault() {

        lazyFlowSubject.reloadAll()

        verify(exactly = 1) {
            lazyListenersSubject.reloadAll(silentMode = false)
        }

    }

    @Test
    fun reloadArgumentDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.reloadArgument("test")

        verify(exactly = 1) {
            lazyListenersSubject.reloadArgument("test")
        }
    }

    @Test
    fun updateAllValuesDelegatesCallToLazyListenersSubject() {

        lazyFlowSubject.updateAllValues("test")

        verify(exactly = 1) {
            lazyListenersSubject.updateAllValues("test")
        }
    }

    @Test
    fun listenDeliversResultsFromCallbackToFlow() = runFlowTest {
        val slot = captureAddListener("arg")

        val flow = lazyFlowSubject.listen("arg")
        val results = flow.startCollecting()
        slot.captured(Pending())
        slot.captured(Success("hi"))

        assertEquals(
            listOf(Pending(), Success("hi")),
            results
        )
    }

    @Test
    fun listenAfterCancellingSubscriptionRemovesCallback() = runFlowTest {
        val slot = captureAddListener("arg")

        val flow = lazyFlowSubject.listen("arg")
        val results = flow.startCollecting()
        slot.captured(Success("111"))
        flow.cancelCollecting()
        slot.captured(Success("222"))

        assertEquals(
            listOf(Success("111")),
            results
        )
        verify(exactly = 1) {
            lazyListenersSubject.removeListener("arg", refEq(slot.captured))
        }
    }

    private fun captureAddListener(arg: String): CapturingSlot<ValueListener<String>> {
        val slot: CapturingSlot<ValueListener<String>> = slot()
        every {
            lazyListenersSubject.addListener(arg, capture(slot))
        } just runs
        return slot
    }


}