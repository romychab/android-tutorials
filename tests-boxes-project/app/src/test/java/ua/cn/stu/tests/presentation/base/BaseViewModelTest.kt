package ua.cn.stu.tests.presentation.base

import io.mockk.impl.annotations.InjectMockKs
import io.mockk.verify
import org.junit.Test
import ua.cn.stu.tests.testutils.ViewModelTest
import ua.cn.stu.tests.testutils.arranged

class BaseViewModelTest : ViewModelTest() {

    @InjectMockKs
    lateinit var viewModel: BaseViewModel

    @Test
    fun logoutCallsLogout() {
        arranged()

        viewModel.logout()

        verify(exactly = 1) {
            accountsRepository.logout()
        }
    }

    @Test
    fun logErrorLogsError() {
        val exception = IllegalStateException()

        viewModel.logError(exception)

        verify(exactly = 1) {
            logger.error(any(), refEq(exception))
        }
    }

}