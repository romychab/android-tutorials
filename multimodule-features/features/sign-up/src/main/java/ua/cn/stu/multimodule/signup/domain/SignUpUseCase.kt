package ua.cn.stu.multimodule.signup.domain

import ua.cn.stu.multimodule.signup.domain.entities.SignUpData
import ua.cn.stu.multimodule.signup.domain.entities.SignUpField
import ua.cn.stu.multimodule.signup.domain.exceptions.AccountAlreadyExistsException
import ua.cn.stu.multimodule.signup.domain.exceptions.EmptyFieldException
import ua.cn.stu.multimodule.signup.domain.exceptions.PasswordMismatchException
import ua.cn.stu.multimodule.signup.domain.repositories.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository,
) {

    /**
     * Create a new account.
     * @throws EmptyFieldException
     * @throws AccountAlreadyExistsException
     * @throws PasswordMismatchException
     */
    suspend fun signUp(signUpData: SignUpData) {
        if (signUpData.email.isBlank()) throw EmptyFieldException(SignUpField.EMAIL)
        if (signUpData.username.isBlank()) throw EmptyFieldException(SignUpField.USERNAME)
        if (signUpData.password.isBlank()) throw EmptyFieldException(SignUpField.PASSWORD)
        if (signUpData.repeatPassword.isBlank()) throw EmptyFieldException(SignUpField.REPEAT_PASSWORD)
        if (signUpData.password != signUpData.repeatPassword) throw PasswordMismatchException()

        signUpRepository.signUp(signUpData)
    }

}