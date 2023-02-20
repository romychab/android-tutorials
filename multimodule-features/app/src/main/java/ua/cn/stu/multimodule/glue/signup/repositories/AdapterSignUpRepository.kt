package ua.cn.stu.multimodule.glue.signup.repositories

import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.data.accounts.entities.SignUpDataEntity
import ua.cn.stu.multimodule.data.accounts.exceptions.AccountAlreadyExistsDataException
import ua.cn.stu.multimodule.signup.domain.entities.SignUpData
import ua.cn.stu.multimodule.signup.domain.exceptions.AccountAlreadyExistsException
import ua.cn.stu.multimodule.signup.domain.repositories.SignUpRepository
import javax.inject.Inject

class AdapterSignUpRepository @Inject constructor(
    private val accountsRepository: AccountsDataRepository
) : SignUpRepository {

    override suspend fun signUp(signUpData: SignUpData) {
        try {
            accountsRepository.signUp(
                SignUpDataEntity(
                    email = signUpData.email,
                    username = signUpData.username,
                    password = signUpData.password,
                )
            )
        } catch (e: AccountAlreadyExistsDataException) {
            throw AccountAlreadyExistsException(e)
        }
    }

}