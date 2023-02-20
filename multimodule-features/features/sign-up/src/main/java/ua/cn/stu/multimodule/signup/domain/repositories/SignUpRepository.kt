package ua.cn.stu.multimodule.signup.domain.repositories

import ua.cn.stu.multimodule.signup.domain.entities.SignUpData
import ua.cn.stu.multimodule.signup.domain.exceptions.AccountAlreadyExistsException

interface SignUpRepository {

    /**
     * @throws AccountAlreadyExistsException if a user with the provided email already exists
     */
    suspend fun signUp(signUpData: SignUpData)

}