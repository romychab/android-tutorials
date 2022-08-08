package ua.cn.stu.tests.domain.accounts.entities

import org.junit.Assert.assertEquals
import org.junit.Test
import ua.cn.stu.tests.domain.EmptyFieldException
import ua.cn.stu.tests.domain.Field
import ua.cn.stu.tests.domain.PasswordMismatchException
import ua.cn.stu.tests.testutils.catch
import ua.cn.stu.tests.testutils.createSignUpData
import ua.cn.stu.tests.testutils.wellDone

class SignUpDataTest {

    @Test
    fun validateForBlankEmailThrowsException() {
        val signUpData = createSignUpData(email = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Email, exception.field)
    }

    @Test
    fun validateForBlankUsernameThrowsException() {
        val signUpData = createSignUpData(username = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Username, exception.field)
    }

    @Test
    fun validateForBlankPasswordThrowsException() {
        val signUpData = createSignUpData(password = "     ")

        val exception: EmptyFieldException = catch { signUpData.validate() }

        assertEquals(Field.Password, exception.field)
    }

    @Test
    fun validateForMismatchedPasswordsThrowsException() {
        val signUpData = createSignUpData(
            password = "p1",
            repeatPassword = "p2"
        )

        catch<PasswordMismatchException> { signUpData.validate() }

        wellDone()
    }

    @Test
    fun validateForValidDataDoesNothing() {
        val signUpData = createSignUpData()

        signUpData.validate()

        wellDone()
    }

}