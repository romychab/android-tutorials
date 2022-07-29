package ua.cn.stu.tests.data.accounts

import com.squareup.moshi.Moshi
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import ua.cn.stu.tests.domain.accounts.entities.Account
import ua.cn.stu.tests.domain.accounts.entities.SignUpData
import ua.cn.stu.tests.data.accounts.entities.*
import ua.cn.stu.tests.data.base.RetrofitConfig
import ua.cn.stu.tests.testutils.createAccount

@ExperimentalCoroutinesApi
class RetrofitAccountsSourceTest {

    @get:Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var accountsApi: AccountsApi

    @Test
    fun signInCallsEndpointAndReturnsToken() = runTest {
        val requestEntity = SignInRequestEntity("email", "password")
        coEvery { accountsApi.signIn(requestEntity) } returns
                SignInResponseEntity("token")
        val source = createSource()

        val token = source.signIn("email", "password")

        assertEquals("token", token)
        coVerify(exactly = 1) {
            accountsApi.signIn(requestEntity)
        }
        confirmVerified(accountsApi)
    }

    @Test
    fun signInWrapsExecutionIntoWrapRetrofitExceptions() = runTest {
        val source = spyk(createSource())
        val requestEntity = SignInRequestEntity("email", "password")
        coEvery { accountsApi.signIn(requestEntity) } returns
                SignInResponseEntity("token")
        val slot: CapturingSlot<suspend () -> String> = slot()
        coEvery { source.wrapRetrofitExceptions(capture(slot)) } returns ""

        source.signIn("email", "password")
        val token = slot.captured.invoke()

        assertEquals("token", token)
    }

    @Test
    fun signUpCallsEndpoint() = runTest {
        val inputEmail = "email"
        val inputUsername = "username"
        val inputPassword = "password"
        val source = createSource()
        coEvery { accountsApi.signUp(any()) } just runs

        val signUpData = SignUpData(inputUsername, inputEmail, inputPassword, inputPassword)
        source.signUp(signUpData)

        val requestEntity = SignUpRequestEntity(inputEmail, inputUsername, inputPassword)
        coVerify(exactly = 1) {
            accountsApi.signUp(requestEntity)
        }
        confirmVerified(accountsApi)
    }

    @Test
    fun signUpWrapsExecutionIntoWrapRetrofitExceptions() = runTest {
        val inputEmail = "email"
        val inputUsername = "username"
        val inputPassword = "password"
        val requestEntity = SignUpRequestEntity(inputEmail, inputUsername, inputPassword)
        val source = spyk(createSource())
        coEvery { accountsApi.signUp(any()) } just runs
        val slot: CapturingSlot<suspend () -> Unit> = slot()
        coEvery { source.wrapRetrofitExceptions(capture(slot)) } just runs

        val signUpData = SignUpData(inputUsername, inputEmail, inputPassword, inputPassword)
        source.signUp(signUpData)

        verify {
            accountsApi wasNot called
        }
        slot.captured.invoke()
        coVerify {
            accountsApi.signUp(requestEntity)
        }
    }

    @Test
    fun getAccountCallsEndpoint() = runTest {
        val source = createSource()
        val getAccountResponse = mockk<GetAccountResponseEntity>()
        val expectedAccount = Account(
            id = 1, username = "username", email = "email", createdAt = 123
        )
        every { getAccountResponse.toAccount() } returns expectedAccount
        coEvery { accountsApi.getAccount() } returns getAccountResponse

        val account = source.getAccount()

        assertSame(expectedAccount, account)
        coVerify(exactly = 1) {
            accountsApi.getAccount()
        }
        confirmVerified(accountsApi)
    }

    @Test
    fun getAccountWrapsExecutionIntoWrapRetrofitExceptions() = runTest {
        val source = spyk(createSource())
        val getAccountResponse = mockk<GetAccountResponseEntity>()
        val expectedAccount = createAccount(
            id = 123, username = "username", email = "email"
        )
        every { getAccountResponse.toAccount() } returns expectedAccount
        coEvery { accountsApi.getAccount() } returns getAccountResponse
        val slot: CapturingSlot<suspend () -> Account> = slot()
        coEvery { source.wrapRetrofitExceptions(capture(slot)) } returns createAccount()

        source.getAccount()
        val account = slot.captured.invoke()

        assertSame(expectedAccount, account)
    }

    @Test
    fun setUsernameCallsEndpoint() = runTest {
        val inputUsername = "username"
        val expectedRequestEntity = UpdateUsernameRequestEntity(inputUsername)
        val source = createSource()
        coEvery { accountsApi.setUsername(any()) } just runs

        source.setUsername(inputUsername)

        coVerify(exactly = 1) {
            accountsApi.setUsername(expectedRequestEntity)
        }
        confirmVerified(accountsApi)
    }

    @Test
    fun setUsernameWrapsExecutionIntoWrapRetrofitExceptions() = runTest {
        val inputUsername = "username"
        val expectedRequestEntity = UpdateUsernameRequestEntity(inputUsername)
        val source = spyk(createSource())
        coEvery { accountsApi.setUsername(any()) } just runs
        val slot: CapturingSlot<suspend () -> Unit> = slot()
        coEvery { source.wrapRetrofitExceptions(capture(slot)) } just runs

        source.setUsername(inputUsername)

        verify {
            accountsApi wasNot called
        }
        slot.captured.invoke()
        coVerify {
            accountsApi.setUsername(expectedRequestEntity)
        }
    }

    private fun createSource() = RetrofitAccountsSource(
        config = RetrofitConfig(
            retrofit = createRetrofit(),
            moshi = Moshi.Builder().build()
        )
    )

    private fun createRetrofit(): Retrofit {
        val retrofit = mockk<Retrofit>()
        every { retrofit.create(AccountsApi::class.java) } returns accountsApi
        return retrofit
    }

}