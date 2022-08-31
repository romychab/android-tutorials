package ua.cn.stu.tests.presentation.splash

import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.utils.requireValue
import ua.cn.stu.tests.testutils.ViewModelTest

class SplashViewModelTest : ViewModelTest() {

    @Test
    fun splashViewModelWithSignedInUserSendsLaunchMainScreenWithTrueValue() {
        val accountsRepository = mockk<AccountsRepository>()
        every { accountsRepository.isSignedIn() } returns true

        val viewModel = SplashViewModel(accountsRepository)

        val isSignedIn = viewModel.launchMainScreenEvent.requireValue().get()!!
        assertTrue(isSignedIn)
    }

    @Test
    fun splashViewModelWithoutSignedInUserSendsLaunchMainScreenWithFalseValue() {
        val accountsRepository = mockk<AccountsRepository>()
        every { accountsRepository.isSignedIn() } returns false

        val viewModel = SplashViewModel(accountsRepository)

        val isSignedIn = viewModel.launchMainScreenEvent.requireValue().get()!!
        assertFalse(isSignedIn)
    }
}