package ua.cn.stu.tests.presentation.main

import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Test
import ua.cn.stu.tests.domain.Empty
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Result
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.domain.accounts.entities.Account
import ua.cn.stu.tests.testutils.ViewModelTest
import ua.cn.stu.tests.testutils.createAccount

class MainActivityViewModelTest : ViewModelTest() {

    @Test
    fun mainViewModelSharesUsernameOfCurrentUser() {
        val account = createAccount(username = "username")
        every { accountsRepository.getAccount() } returns flowOf(Success(account))
        val viewModel = MainActivityViewModel(accountsRepository)

        val username = viewModel.username.value

        assertEquals("@username", username)
    }

    @Test
    fun mainViewModelSharesEmptyStringIfCurrentUserUnavailable() {
        every { accountsRepository.getAccount() } returns flowOf(Empty())
        val viewModel = MainActivityViewModel(accountsRepository)

        val username = viewModel.username.value

        assertEquals("", username)
    }

    @Test
    fun mainViewModelListensForFurtherUsernameUpdates() {
        val flow: MutableStateFlow<Result<Account>> =
            MutableStateFlow(Success(createAccount(username = "username1")))
        every { accountsRepository.getAccount() } returns flow
        val viewModel = MainActivityViewModel(accountsRepository)

        val username1 = viewModel.username.value
        flow.value = Pending()
        val username2 = viewModel.username.value
        flow.value = Success(createAccount(username = "username2"))
        val username3 = viewModel.username.value

        assertEquals("@username1", username1)
        assertEquals("", username2)
        assertEquals("@username2", username3)
    }

}