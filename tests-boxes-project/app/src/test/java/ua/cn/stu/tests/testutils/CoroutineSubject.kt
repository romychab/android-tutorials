package ua.cn.stu.tests.testutils

import io.mockk.MockKStubScope
import kotlinx.coroutines.sync.Mutex
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * DSL extension that allows returning [CoroutineSubject] as an answer
 * for any suspend method.
 * Usage example:
 * ```
 *   val subject = CoroutineSubject<Account>()
 *
 *   // mock repository method for loading account
 *   coEvery { accountsRepository.loadAccount() } returnsSubject subject
 *
 *   // assuming fetchAccount uses AccountsRepository.loadAccount
 *   viewModel.fetchAccount()
 *
 *   // assert that view-model shows progress-bar while loading account
 *   assertTrue(viewModel.isAccountLoading)
 *   // account has been loaded:
 *   subject.sendSuccess(createSomeTestAccount())
 *   // assert that view-model doesn't show progress-bar after the
 *   // account has been loaded
 *   assertFalse(viewModel.isAccountLoading)
 * ```
 */
infix fun <T, B> MockKStubScope<T, B>.returnsSubject(subject: CoroutineSubject<T>) =
    coAnswers { subject.get() }

/**
 * Coroutine which execution can be controlled outside. May be
 * useful for testing purposes.
 */
class CoroutineSubject<T> {

    private val mutex = Mutex(locked = true)
    private lateinit var value: Value<T>

    suspend fun get(): T {
        mutex.lock()
        value.let {
            if (it is Value.Success) return it.data
            if (it is Value.Error) throw it.error
        }
        throw IllegalStateException("Invalid value type")
    }

    fun sendSuccess(data: T) {
        value = Value.Success(data)
        mutex.unlock()
    }

    fun sendError(error: Throwable) {
        value = Value.Error(error)
        mutex.unlock()
    }

    sealed class Value<T> {
        class Error<T>(val error: Throwable) : Value<T>()
        class Success<T>(val data: T): Value<T>()
    }

}


/**
 * Another implementation variant for the same logic. It uses
 * Continuation instead of Mutex.
 */
class AnotherCoroutineSubject<T> {

    private var continuation: Continuation<T>? = null

    suspend fun get(): T = suspendCoroutine {
        this.continuation = it
    }

    fun sendError(e: Exception) {
        continuation?.resumeWithException(e)
        continuation = null
    }

    fun sendSuccess(value: T) {
        continuation?.resume(value)
    }

}
