package ua.cn.stu.tests.domain

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.cn.stu.tests.testutils.catch
import ua.cn.stu.tests.testutils.wellDone

@ExperimentalCoroutinesApi
class ExceptionsTest {

    lateinit var block: suspend () -> String

    @Before
    fun setUp() {
        block = mockk()
    }

    @Test
    fun wrapBackendExceptionsRethrowsOtherExceptions() = runTest {
        val expectedException = IllegalStateException()
        coEvery { block() } throws expectedException

        val exception: IllegalStateException = catch {
            wrapBackendExceptions(block)
        }

        assertSame(expectedException, exception)
    }

    @Test
    fun wrapBackendExceptionsMaps401ErrorToAuthException() = runTest {
        coEvery { block() } throws BackendException(
            code = 401,
            message = "Oops"
        )

        catch<AuthException> { wrapBackendExceptions(block) }

        wellDone()
    }

    @Test
    fun wrapBackendExceptionDoesNotMapOtherBackendExceptions() = runTest {
        val expectedBackendException = BackendException(
            code = 432,
            message = "Boom!"
        )
        coEvery { block() } throws expectedBackendException

        val exception: BackendException = catch {
            wrapBackendExceptions(block)
        }

        assertSame(expectedBackendException, exception)
    }
}