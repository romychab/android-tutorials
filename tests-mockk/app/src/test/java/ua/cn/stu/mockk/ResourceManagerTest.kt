package ua.cn.stu.mockk

import io.mockk.*
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.Executor

class ResourceManagerTest {

    @Test
    fun consumeResourceAfterSetResourceCallReceivesResource() {
        // arrange
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        // act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        // assert
        verify(exactly = 1) { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceCallsAfterSetResourceCallReceiveResourceForEachConsumer() {
        // arrange
        val resourceManager = createResourceManager()
        val consumer1 = createConsumer()
        val consumer2 = createConsumer()

        // act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)

        // assert
        verifySequence {
            consumer1("TEST")
            consumer2("TEST")
        }
    }

    @Test
    fun consumeResourceAfterSetResourceCallsReceivesLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        verify(exactly = 1) { consumer("TEST2") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveTheSameResource() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)
        resourceManager.consumeResource(consumer)

        verify(exactly = 2) { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceWithoutActiveResourceDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)

        verify { consumer wasNot called }
    }

    @Test
    fun setResourceAfterConsumeResourceCallDeliversResourceToConsumer() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        verify(exactly = 1) { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceReceivesResourceOnlyOnce() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        verify(exactly = 1) { consumer("TEST1") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveMultipleResources() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        verifySequence {
            consumer("TEST1")
            consumer("TEST2")
        }
    }

    @Test
    fun setResourceAfterMultipleConsumeResourceCallsDeliversResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer1 = createConsumer()
        val consumer2 = createConsumer()

        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)
        resourceManager.setResource("TEST")

        verifySequence {
            consumer1("TEST")
            consumer2("TEST")
        }
    }

    @Test
    fun setResourceCallsAfterConsumeResourceCallDeliversTheFirstResourceOnce() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")

        verify(exactly = 1) { consumer("TEST1") }
        confirmVerified(consumer)
    }

    @Test
    fun setResourceBetweenConsumeResourceCallsDeliversTheSameResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        verify(exactly = 2) { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun setResourceDoubleCallBetweenConsumeResourceCallsDeliversDifferentResources() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        verifySequence {
            consumer("TEST1")
            consumer("TEST2")
        }
    }

    @Test
    fun consumeResourceAfterClearResourceCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)

        verify { consumer wasNot called }
    }

    @Test
    fun consumeResourceAfterClearResourceAndSetResourceCallsReceivesLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        verify(exactly = 1) { consumer("TEST2") }
        confirmVerified(consumer)
    }

    @Test
    fun setResourceAfterConsumeResourceAndClearResourceCallsDeliversLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        verify(exactly = 1) { consumer("TEST2") }
        confirmVerified(consumer)
    }

    @Test
    fun destroyClearsCurrentResource() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.destroy()
        resourceManager.consumeResource(consumer)

        verify { consumer wasNot called }
    }

    @Test
    fun destroyClearsPendingConsumers() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.destroy()
        resourceManager.setResource("TEST")

        verify { consumer wasNot called }
    }

    @Test
    fun setResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.destroy()
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        verify { consumer wasNot called }
    }

    @Test
    fun consumeResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.destroy()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        verify { consumer wasNot called }
    }

    @Test(expected = Test.None::class)
    fun setResourceHandlesConcurrentConsumersModification() {
        val resourceManager = createResourceManager()
        val consumer = createConsumer()

        resourceManager.consumeResource {
            resourceManager.clearResource()
            resourceManager.consumeResource(consumer)
        }
        resourceManager.setResource("TEST")

        verify { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun consumeResourceDeliversExceptionsToErrorHandler() {
        val errorHandler: ErrorHandler<String> = mockk()
        every { errorHandler.onError(any(), any()) } just runs
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.setResource("TEST")
        resourceManager.consumeResource { resource ->
            throw expectedException
        }

        verify(exactly = 1) {
            errorHandler.onError(refEq(expectedException), "TEST")
        }
        confirmVerified(errorHandler)
    }

    @Test
    fun setResourceDeliversExceptionsToErrorHandler() {
        val errorHandler: ErrorHandler<String> = mockk()
        every { errorHandler.onError(any(), any()) } just runs
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.consumeResource { resource ->
            throw expectedException
        }
        resourceManager.setResource("TEST")

        verify(exactly = 1) {
            errorHandler.onError(refEq(expectedException), "TEST")
        }
        confirmVerified(errorHandler)
    }

    @Test
    fun consumeResourceDoesNotInvokeConsumerOutsideOfExecutor() {
        val executor: Executor = mockk()
        every { executor.execute(any()) } just runs
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        verify(exactly = 1) {
            executor.execute(any())
            consumer wasNot called
        }
        confirmVerified(executor, consumer)
    }

    @Test
    fun consumeResourceInvokesConsumerInExecutor() {
        val executor: Executor = mockk()
        val commandSlot = slot<Runnable>()
        every { executor.execute(capture(commandSlot)) } just runs
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = createConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertTrue(commandSlot.isCaptured)
        commandSlot.captured.run()
        verify(exactly = 1) { consumer("TEST") }
        confirmVerified(consumer)
    }

    @Test
    fun setResourceInvokesPendingConsumerInExecutor() {
        val executor: Executor = mockk()
        val commandSlot = slot<Runnable>()
        every { executor.execute(capture(commandSlot)) } just runs
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = createConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertTrue(commandSlot.isCaptured)
        commandSlot.captured.run()
        verify(exactly = 1) { consumer("TEST") }
        confirmVerified(consumer)
    }

    private fun createResourceManager(
        executor: Executor = immediateExecutor(),
        errorHandler: ErrorHandler<String> = dummyErrorHandler()
    ): ResourceManager<String> {
        return ResourceManager(executor, errorHandler)
    }

    private fun dummyErrorHandler(): ErrorHandler<String> = mockk()

    private fun immediateExecutor(): Executor {
        val executor = mockk<Executor>()
        every { executor.execute(any()) } answers {
            firstArg<Runnable>().run()
        }
        return executor
    }

    private fun createConsumer(): Consumer<String> = mockk(relaxed = true)


}