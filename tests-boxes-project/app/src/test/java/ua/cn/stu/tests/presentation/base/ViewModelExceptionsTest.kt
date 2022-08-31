package ua.cn.stu.tests.presentation.base

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import ua.cn.stu.tests.R
import ua.cn.stu.tests.domain.AuthException
import ua.cn.stu.tests.domain.BackendException
import ua.cn.stu.tests.domain.ConnectionException
import ua.cn.stu.tests.utils.requireValue
import ua.cn.stu.tests.testutils.ViewModelTest

abstract class ViewModelExceptionsTest<VM : BaseViewModel> : ViewModelTest() {

    abstract val viewModel: VM

    abstract fun arrangeWithException(e: Exception)

    abstract fun act()

    @Test
    fun safeLaunchWithConnectionExceptionShowsMessage() {
        val exception = ConnectionException(Exception())
        arrangeWithException(exception)

        act()

        assertEquals(
            R.string.connection_error,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithBackendExceptionShowsMessage() {
        val exception = BackendException(404, "Some error message")
        arrangeWithException(exception)

        act()

        assertEquals(
            exception.message,
            viewModel.showErrorMessageEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithAuthExceptionRestartsFromLoginScreen() {
        val exception = AuthException(Exception())
        arrangeWithException(exception)

        act()

        assertNotNull(
            viewModel.showAuthErrorAndRestartEvent.requireValue().get()
        )
    }

    @Test
    fun safeLaunchWithOtherExceptionsShowsInternalErrorMessage() {
        val exception = IllegalStateException()
        arrangeWithException(exception)

        act()

        assertEquals(
            R.string.internal_error,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

}