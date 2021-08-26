package ua.cn.stu.foundation.model.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Holder for coroutine dispatcher which should be used for IO-intensive operations
 */
class IoDispatcher(
    val value: CoroutineDispatcher = Dispatchers.IO
)

/**
 * Holder for coroutine dispatcher which should be used for CPU-intensive operations
 */
class WorkerDispatcher(
    val value: CoroutineDispatcher = Dispatchers.Default
)
