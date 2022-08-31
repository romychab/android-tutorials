package ua.cn.stu.tests.presentation.main.tabs.profile

import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ua.cn.stu.tests.R
import ua.cn.stu.tests.domain.*
import ua.cn.stu.tests.domain.accounts.entities.Account
import ua.cn.stu.tests.presentation.base.ViewModelExceptionsTest
import ua.cn.stu.tests.utils.requireValue
import ua.cn.stu.tests.testutils.*

class EditProfileViewModelTest : ViewModelTest() {

    private lateinit var flow: MutableStateFlow<Result<Account>>

    private lateinit var viewModel: EditProfileViewModel

    @Before
    fun setUp() {
        flow = MutableStateFlow(Pending())
        every { accountsRepository.getAccount() } returns flow
        viewModel = EditProfileViewModel(accountsRepository, logger)
    }

    @Test
    fun saveUsernameShowsProgress() {
        coEvery { accountsRepository.updateAccountUsername(any()) } returnsSubject
                CoroutineSubject()

        viewModel.saveUsername("username")

        assertTrue(viewModel.saveInProgress.requireValue())
    }

    @Test
    fun saveUsernameSendsUsernameToRepository() {
        coEvery { accountsRepository.updateAccountUsername(any()) } returnsSubject
                CoroutineSubject()

        viewModel.saveUsername("username")

        coVerify(exactly = 1) {
            accountsRepository.updateAccountUsername("username")
        }
    }

    @Test
    fun saveUsernameWithSuccessHidesProgressAndGoesBack() {
        coEvery { accountsRepository.updateAccountUsername(any()) } just runs

        viewModel.saveUsername("username")

        assertFalse(viewModel.saveInProgress.requireValue())
        assertNotNull(viewModel.goBackEvent.requireValue().get())
    }

    @Test
    fun saveUsernameWithErrorHidesProgress() {
        coEvery { accountsRepository.updateAccountUsername(any()) } throws
                IllegalStateException()

        viewModel.saveUsername("username")

        assertFalse(viewModel.saveInProgress.requireValue())
        assertNull(viewModel.goBackEvent.value?.get())
    }

    @Test
    fun saveUsernameWithEmptyValueShowsError() {
        coEvery { accountsRepository.updateAccountUsername(any()) } throws
                EmptyFieldException(Field.Username)

        viewModel.saveUsername("username")

        assertEquals(
            R.string.field_is_empty,
            viewModel.showErrorMessageResEvent.requireValue().get()
        )
    }

    @Test
    fun initialUsernameEventReturnsFirstValueFromRepository() {
        arranged()

        flow.value = Success(createAccount(username = "username1"))
        val value1 = viewModel.initialUsernameEvent.value?.get()
        flow.value = Success(createAccount(username = "username2"))
        val value2 = viewModel.initialUsernameEvent.value?.get()

        assertEquals("username1", value1)
        assertNull(value2)
    }

    class SaveUsernameExceptionsTest : ViewModelExceptionsTest<EditProfileViewModel>() {

        override lateinit var viewModel: EditProfileViewModel

        @Before
        fun setUp() {
            every { accountsRepository.getAccount() } returns
                    flowOf(Success(createAccount()))
            viewModel = EditProfileViewModel(accountsRepository, logger)
        }

        override fun arrangeWithException(e: Exception) {
            coEvery { accountsRepository.updateAccountUsername(any()) } throws e
        }

        override fun act() {
            viewModel.saveUsername("username")
        }
    }

}