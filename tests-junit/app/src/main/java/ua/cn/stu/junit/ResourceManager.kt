package ua.cn.stu.junit

import java.util.concurrent.Executor

/**
 * Resource manager can hold 1 instance of some resource <R> at a time.
 * - A resource can be assigned/replaced by using [setResource] call.
 * - A resource can be unassigned by [clearResource] call.
 * - A resource can be accessed via [consumeResource] call.
 * - A [ResourceManager] instance can be destroyed by [destroy] call.
 * For more details: see comments for methods [setResource],
 * [clearResource], [consumeResource] and [destroy].
 */
class ResourceManager<R>(

    /**
     * Executor which launches resource consumers passed
     * to [consumeResource] call.
     */
    private val executor: Executor,

    /**
     * Error handler which intercepts exceptions occurred upon
     * consuming resources.
     */
    private val errorHandler: ErrorHandler<R>

) {

    private val consumers = mutableListOf<Consumer<R>>()
    private var resource: R? = null
    private var destroyed = false

    /**
     * Assign/replace the resource held by the resource manager.
     *
     * If there are pending consumers added via [consumeResource]
     * call, then they are called via [executor] in the same order as they
     * have been added to the [consumeResource] call. The process of
     * consuming the resource is the same as for [consumeResource] call
     * when the resource is available.
     *
     * If the manager has been already destroyed, this method does nothing.
     */
    fun setResource(resource: R) = synchronized(this) {
        if (destroyed) return
        var localConsumers: List<Consumer<R>>
        do {
            localConsumers = ArrayList(consumers)
            consumers.clear()
            localConsumers.forEach { consumer ->
                processResource(consumer, resource)
            }
        } while (consumers.isNotEmpty())
        this.resource = resource
    }

    /**
     * Remove the resource (if exists) from this resource manager.
     * If the manager has been already destroyed, this method does nothing.
     */
    fun clearResource() = synchronized(this) {
        if (destroyed) return
        this.resource = null
    }

    /**
     * Access the resource by the specified [consumer].
     *
     * - If the resource is available at the moment of [consumeResource] call,
     * then the [consumer] is called immediately via [executor], and
     * the resource is sent to the consumer's argument.
     * - If the resource is NOT available, the [consumer] starts waiting
     * for the resource until it becomes available or until the manager
     * gets destroyed.
     *
     * Consumers may fail with exceptions. In this case the resource manager
     * should call [ErrorHandler.onError] on [errorHandler] instance passed to
     * the constructor.
     *
     * Each call of [consumeResource] gives an ability to receive the resource ONLY ONCE.
     * For example, this code prints "Hello, R1" 1 time:
     * ```
     *   resourceManager.consumeResource { resource ->
     *     println("Hello, $resource")
     *   }
     *   resourceManager.setResource("R1")
     *   resourceManager.setResource("R2")
     * ```
     *
     * This code prints "Hello, R2" 1 time:
     * ```
     *   resourceManager.setResource("R1")
     *   resourceManager.setResource("R2")
     *   resourceManager.consumeResource { resource ->
     *     println("Hello, $resource")
     *   }
     *   resourceManager.setResource("R3")
     * ```
     *
     * But 2 consumers may access the same resource. For example this code
     * prints "Hello, R1" 2 times:
     * ```
     *   val consumer: Consumer<String> = { resource ->
     *     println("Hello, $resource")
     *   }
     *   resourceManager.setResource("R1")
     *   resourceManager.consumeResource(consumer) // "Hello, R1"
     *   resourceManager.consumeResource(consumer) // "Hello, R1" again
     * ```
     *
     * If the manager has been already destroyed, this method does nothing.
     */
    fun consumeResource(consumer: Consumer<R>) = synchronized(this) {
        if (destroyed) return@synchronized
        val resource = this.resource
        if (resource != null) {
            processResource(consumer, resource)
        } else {
            consumers.add(consumer)
        }
    }

    /**
     * Destroy this resource manager.
     *
     * All further calls of [setResource], [clearResource] and
     * [consumeResource] are ignored after destroying. All pending consumers
     * passed to [consumeResource] are discarded.
     * If there is a resource assigned via [setResource] call at the
     * moment of destroying, then it is cleared in the same way as
     * the [clearResource] call does.
     */
    fun destroy() = synchronized(this) {
        destroyed = true
        consumers.clear()
        resource = null
    }

    private fun processResource(consumer: Consumer<R>, resource: R) {
        executor.execute {
            try {
                consumer.invoke(resource)
            } catch (e: Exception) {
                errorHandler.onError(e, resource)
            }
        }
    }

}