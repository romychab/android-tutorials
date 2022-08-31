package ua.cn.stu.tests.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import org.junit.Rule
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.utils.logger.Logger

open class ViewModelTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

    @RelaxedMockK
    lateinit var logger: Logger

    @RelaxedMockK
    lateinit var accountsRepository: AccountsRepository

}