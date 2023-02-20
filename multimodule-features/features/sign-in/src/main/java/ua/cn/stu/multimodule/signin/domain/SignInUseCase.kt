package ua.cn.stu.multimodule.signin.domain

import ua.cn.stu.multimodule.signin.domain.exceptions.EmptyEmailException
import ua.cn.stu.multimodule.signin.domain.exceptions.EmptyPasswordException
import ua.cn.stu.multimodule.signin.domain.repositories.AuthServiceRepository
import ua.cn.stu.multimodule.signin.domain.repositories.AuthTokenRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authTokenRepository: AuthTokenRepository,
    private val authServiceRepository: AuthServiceRepository,
) {

    /**
     * Sign-in to the app by login and password and save auth token.
     * @throws EmptyEmailException if email is blank
     * @throws EmptyPasswordException if password is blank
     */
    suspend fun signIn(email: String, password: String) {
        if (email.isBlank()) throw EmptyEmailException()
        if (password.isBlank()) throw EmptyPasswordException()

        val token = authServiceRepository.signIn(email, password)

        authTokenRepository.setToken(token)
    }

}