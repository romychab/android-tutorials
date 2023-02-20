package ua.cn.stu.multimodule.glue.signin.repositories

import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.signin.domain.repositories.AuthServiceRepository
import javax.inject.Inject

class AdapterAuthServiceRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
) : AuthServiceRepository {

    override suspend fun signIn(email: String, password: String): String {
        return accountsDataRepository.signIn(email, password)
    }

}