package ua.cn.stu.junit

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Executor

class ResourceManagerTest {

    @Test
    fun consumeResourceAfterSetResourceCallReceivesResource() {
        // arrange
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        // act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        // assert
        assertEquals("TEST", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceCallsAfterSetResourceCallReceiveResourceForEachConsumer() {
        // arrange
        val resourceManager = createResourceManager()
        val consumer1 = TestConsumer()
        val consumer2 = TestConsumer()

        // act
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)

        // assert
        assertEquals("TEST", consumer1.lastResource)
        assertEquals(1, consumer1.invokeCount)
        assertEquals("TEST", consumer2.lastResource)
        assertEquals(1, consumer2.invokeCount)
    }

    @Test
    fun consumeResourceAfterSetResourceCallsReceivesLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals("TEST2", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveTheSameResource() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)
        resourceManager.consumeResource(consumer)

        assertEquals("TEST", consumer.resources[0])
        assertEquals("TEST", consumer.resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun consumeResourceWithoutActiveResourceDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterConsumeResourceCallDeliversResourceToConsumer() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals("TEST", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceReceivesResourceOnlyOnce() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals("TEST1", consumer.lastResource)
        assertEquals(1, consumer.invokeCount)
    }

    @Test
    fun consumeResourceCallsWithSameConsumerCanReceiveMultipleResources() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
        assertEquals(2, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterMultipleConsumeResourceCallsDeliversResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer1 = TestConsumer()
        val consumer2 = TestConsumer()

        resourceManager.consumeResource(consumer1)
        resourceManager.consumeResource(consumer2)
        resourceManager.setResource("TEST")

        assertEquals("TEST", consumer1.lastResource)
        assertEquals("TEST", consumer2.lastResource)
        assertEquals(1, consumer1.invokeCount)
        assertEquals(1, consumer2.invokeCount)
    }

    @Test
    fun setResourceCallsAfterConsumeResourceCallDeliversTheFirstResourceOnce() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST1", consumer.lastResource)
    }

    @Test
    fun setResourceBetweenConsumeResourceCallsDeliversTheSameResourceToAllConsumers() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST", consumer.resources[0])
        assertEquals("TEST", consumer.resources[1])
    }

    @Test
    fun setResourceDoubleCallBetweenConsumeResourceCallsDeliversDifferentResources() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST1")
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(2, consumer.invokeCount)
        assertEquals("TEST1", consumer.resources[0])
        assertEquals("TEST2", consumer.resources[1])
    }

    @Test
    fun consumeResourceAfterClearResourceCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun consumeResourceAfterClearResourceAndSetResourceCallsReceivesLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.setResource("TEST2")
        resourceManager.consumeResource(consumer)

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun setResourceAfterConsumeResourceAndClearResourceCallsDeliversLatestResource() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST1")
        resourceManager.clearResource()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST2")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST2", consumer.lastResource)
    }

    @Test
    fun destroyClearsCurrentResource() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.destroy()
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun destroyClearsPendingConsumers() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.destroy()
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun setResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.destroy()
        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(0, consumer.invokeCount)
    }

    @Test
    fun consumeResourceAfterDestroyCallDoesNothing() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.destroy()
        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals(0, consumer.invokeCount)
    }

    @Test(expected = Test.None::class)
    fun setResourceHandlesConcurrentConsumersModification() {
        val resourceManager = createResourceManager()
        val consumer = TestConsumer()

        resourceManager.consumeResource {
            resourceManager.clearResource()
            resourceManager.consumeResource(consumer)
        }
        resourceManager.setResource("TEST")

        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun consumeResourceDeliversExceptionsToErrorHandler() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.setResource("TEST")
        resourceManager.consumeResource { resource ->
            throw expectedException
        }

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    @Test
    fun setResourceDeliversExceptionsToErrorHandler() {
        val errorHandler = TestErrorHandler()
        val resourceManager = createResourceManager(
            errorHandler = errorHandler
        )
        val expectedException = IllegalStateException("Test exception")

        resourceManager.consumeResource { resource ->
            throw expectedException
        }
        resourceManager.setResource("TEST")

        assertEquals(1, errorHandler.invokeCount)
        assertEquals(
            TestErrorHandler.Record(expectedException, "TEST"),
            errorHandler.records[0]
        )
    }

    /*@Test
    fun consumeResourceDoesNotInvokeConsumerOutsideOfExecutor() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(1, executor.invokeCount)
        assertEquals(0, consumer.invokeCount)
    }*/

    @Test
    fun consumeResourceInvokesConsumerInExecutor() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = TestConsumer()

        resourceManager.setResource("TEST")
        resourceManager.consumeResource(consumer)

        assertEquals(1, executor.invokeCount)
        assertEquals(0, consumer.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }

    @Test
    fun setResourceInvokesPendingConsumerInExecutor() {
        val executor = TestExecutor(autoExec = false)
        val resourceManager = createResourceManager(
            executor = executor
        )
        val consumer = TestConsumer()

        resourceManager.consumeResource(consumer)
        resourceManager.setResource("TEST")

        assertEquals(1, executor.invokeCount)
        assertEquals(0, consumer.invokeCount)
        executor.commands[0].run()
        assertEquals(1, consumer.invokeCount)
        assertEquals("TEST", consumer.lastResource)
    }


    private fun createResourceManager(
        executor: Executor = TestExecutor(),
        errorHandler: ErrorHandler<String> = TestErrorHandler()
    ): ResourceManager<String> {
        return ResourceManager(executor, errorHandler)
    }


}