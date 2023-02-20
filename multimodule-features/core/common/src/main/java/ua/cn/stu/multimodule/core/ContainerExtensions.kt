package ua.cn.stu.multimodule.core

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout

/**
 * Try to get the first non-pending value from the flow emitting [Container]
 * entities.
 *
 * Returns first [T] instance emitted by the flow within the specified
 * [timeoutMillis].
 *
 * Throws an exception from the container if the latter is [Container.Error].
 *
 * Throws [TimeoutCancellationException] if the flow hasn't emitted a success or error
 * value during [timeoutMillis] milliseconds.
 */
suspend fun <T> Flow<Container<T>>.unwrapFirst(timeoutMillis: Long = Core.remoteTimeoutMillis): T {
    return withTimeout(timeoutMillis) {
        filterNot { it is Container.Pending }
            .first()
            .unwrap()
    }
}