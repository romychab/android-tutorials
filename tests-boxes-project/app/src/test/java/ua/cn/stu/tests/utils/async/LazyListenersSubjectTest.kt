package ua.cn.stu.tests.utils.async

import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.junit.Rule
import org.junit.Test
import ua.cn.stu.tests.domain.Empty
import ua.cn.stu.tests.domain.Error
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.testutils.immediateExecutorService
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LazyListenersSubjectTest {

    @get:Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var loader: ValueLoader<String, String>

    @RelaxedMockK
    lateinit var listener: ValueListener<String>

    @Test
    fun addListenerTriggersValueLoadingOnlyOnceForSameArgument() {
        val subject = createSubject()
        every { loader(any()) } returns "result"

        subject.addListener("arg1", listener)
        subject.addListener("arg1", listener)
        subject.addListener("arg2", listener)
        subject.addListener("arg2", listener)

        verify(exactly = 1) {
            loader("arg1")
            loader("arg2")
        }
    }

    @Test
    fun addListenerDeliversLoadResultsToListener() {
        val subject = createSubject()
        every { loader(any()) } returns "result"

        subject.addListener("arg", listener)

        verifySequence {
            listener(Pending())
            listener(Success("result"))
        }
    }

    @Test
    fun addListenerWithNullValueFromLoaderDeliversEmptyResultToListener() {
        val subject = createSubject()
        every { loader(any()) } returns null

        subject.addListener("arg", listener)

        verifySequence {
            listener(Pending())
            listener(Empty())
        }
    }

    @Test
    fun addListenerWithFailedLoaderDeliversErrorResultToListener() {
        val expectedException = IllegalStateException()
        val subject = createSubject()
        every { loader(any()) } throws expectedException

        subject.addListener("arg", listener)

        verifySequence {
            listener(Pending())
            listener(Error(expectedException))
        }
    }

    @Test
    fun addListenerDeliversCurrentResultsImmediately() {
        val firstListener = mockk<ValueListener<String>>(relaxed = true)
        val subject = createSubject()
        every { loader(any()) } returns "result"

        subject.addListener("arg", firstListener)
        subject.addListener("arg", listener)

        verify(exactly = 1) {
            listener(Success("result"))
        }
        confirmVerified(listener)
    }

    @Test
    fun addListenerDeliversPendingResultForNonFinishedLoad() {
        val awaitLoaderExecutionStart = CountDownLatch(1)
        val awaitLoaderExecutionFinish = CountDownLatch(1)
        every { loader(any()) } answers {
            awaitLoaderExecutionStart.countDown()
            awaitLoaderExecutionFinish.await(1, TimeUnit.SECONDS)
            "result"
        }
        val subject = createSubject(
            loaderExecutor = Executors.newSingleThreadExecutor()
        )

        subject.addListener("arg", listener)
        awaitLoaderExecutionStart.await(1, TimeUnit.SECONDS)

        verifySequence {
            listener(Pending())
        }
        awaitLoaderExecutionFinish.countDown()
        verify(exactly = 1) {
            listener(Success("result"))
        }
        confirmVerified(listener)
    }

    @Test
    fun removeListenerCancelsLoadingAfterRemovingTheLastListener() {
        val awaitLoaderExecutionStart = CountDownLatch(1)
        val awaitLoaderExecutionFinish = CountDownLatch(1)
        every { loader(any()) } answers {
            awaitLoaderExecutionStart.countDown()
            awaitLoaderExecutionFinish.await(1, TimeUnit.SECONDS)
            "result"
        }
        val subject = createSubject(
            loaderExecutor = Executors.newSingleThreadExecutor()
        )

        subject.addListener("arg", listener)
        awaitLoaderExecutionStart.await(1, TimeUnit.SECONDS)
        subject.removeListener("arg", listener)
        awaitLoaderExecutionFinish.countDown()

        verifySequence {
            listener(Pending())
        }
    }

    @Test
    fun removeListenerStopsDeliveringResultsViaReloadAllToListener() {
        val subject = createSubject()
        every { loader(any()) } returns "result1" andThen "result1-updated"

        subject.addListener("arg", listener)
        subject.removeListener("arg", listener)
        subject.reloadAll()

        verifySequence {
            listener(Pending())
            listener(Success("result1"))
        }
    }

    @Test
    fun removeListenerStopsDeliveringResultsViaReloadArgumentToListener() {
        val subject = createSubject()
        every { loader(any()) } returns "result1" andThen "result1-updated"

        subject.addListener("arg", listener)
        subject.removeListener("arg", listener)
        subject.reloadArgument("arg")

        verifySequence {
            listener(Pending())
            listener(Success("result1"))
        }
    }

    @Test
    fun removeListenerStopsDeliveringResultsViaUpdateAllToListener() {
        val subject = createSubject()
        every { loader(any()) } returns "result1"

        subject.addListener("arg", listener)
        subject.removeListener("arg", listener)
        subject.updateAllValues("result1-updated")

        verifySequence {
            listener(Pending())
            listener(Success("result1"))
        }
    }

    @Test
    fun reloadAllTriggersReloadingForAllActiveArguments() {
        val subject = createSubject()
        val listener1: ValueListener<String> = mockk(relaxed = true)
        val listener2: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg1") } returns "result1" andThen "result1-updated"
        every { loader("arg2") } returns "result2" andThen "result2-updated"

        subject.addListener("arg1", listener1)
        subject.addListener("arg2", listener2)
        subject.reloadAll()

        verifySequence {
            listener1(Pending())
            listener1(Success("result1"))
            listener1(Pending())
            listener1(Success("result1-updated"))
        }
        verifySequence {
            listener2(Pending())
            listener2(Success("result2"))
            listener2(Pending())
            listener2(Success("result2-updated"))
        }
    }


    @Test
    fun reloadAllWithSilendModeTriggersReloadingForAllActiveArgumentsWithoutEmittingPendingStatus() {
        val subject = createSubject()
        val listener1: ValueListener<String> = mockk(relaxed = true)
        val listener2: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg1") } returns "result1" andThen "result1-updated"
        every { loader("arg2") } returns "result2" andThen "result2-updated"

        subject.addListener("arg1", listener1)
        subject.addListener("arg2", listener2)
        subject.reloadAll(silentMode = true)

        verifySequence {
            listener1(Pending())
            listener1(Success("result1"))
            listener1(Success("result1-updated"))
        }
        verifySequence {
            listener2(Pending())
            listener2(Success("result2"))
            listener2(Success("result2-updated"))
        }
    }

    @Test
    fun reloadArgumentDoesNothingForNonExistingArguments() {
        val subject = createSubject()
        every { loader(any()) } returns "result" andThen "result-updated"

        subject.addListener("arg1", listener)
        subject.reloadArgument("other-arg")

        verifySequence {
            listener(Pending())
            listener(Success("result"))
        }
        verify(exactly = 1) {
            loader(any())
        }
    }

    @Test
    fun reloadArgumentReloadsSpecificArgument() {
        val subject = createSubject()
        val listener1: ValueListener<String> = mockk(relaxed = true)
        val listener2: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg1") } returns "result1" andThen "result1-updated"
        every { loader("arg2") } returns "result2" andThen "result2-updated"

        subject.addListener("arg1", listener1)
        subject.addListener("arg2", listener2)
        subject.reloadArgument("arg1")

        verifySequence {
            listener1(Pending())
            listener1(Success("result1"))
            listener1(Pending())
            listener1(Success("result1-updated"))
        }
        verifySequence {
            listener2(Pending())
            listener2(Success("result2"))
        }
    }

    @Test
    fun reloadArgumentWithSilentModeReloadsSpecificArgumentWithoutEmittingPendingStatus() {
        val subject = createSubject()
        val listener1: ValueListener<String> = mockk(relaxed = true)
        val listener2: ValueListener<String> = mockk(relaxed = true)
        every { loader("arg1") } returns "result1" andThen "result1-updated"
        every { loader("arg2") } returns "result2" andThen "result2-updated"

        subject.addListener("arg1", listener1)
        subject.addListener("arg2", listener2)
        subject.reloadArgument("arg1", silentMode = true)

        verifySequence {
            listener1(Pending())
            listener1(Success("result1"))
            listener1(Success("result1-updated"))
        }
        verifySequence {
            listener2(Pending())
            listener2(Success("result2"))
        }
    }

    @Test
    fun updateAllValuesUpdatesAllResultsImmediately() {
        val subject = createSubject()
        every { loader(any()) } returns "result"

        subject.addListener("arg", listener)
        subject.updateAllValues("new-result")

        verifyOrder {
            listener(Success("result"))
            listener(Success("new-result"))
        }
    }

    @Test
    fun updateAllValuesWithNullChangesAllResultsToEmpty() {
        val subject = createSubject()
        every { loader(any()) } returns "result"

        subject.addListener("arg", listener)
        subject.updateAllValues(null)

        verifyOrder {
            listener(Success("result"))
            listener(Empty())
        }
    }

    private fun createSubject(
        loaderExecutor: ExecutorService = immediateExecutorService()
    ) = LazyListenersSubject(
        loaderExecutor = loaderExecutor,
        handlerExecutor = immediateExecutorService(),
        loader = loader
    )
}