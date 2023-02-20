package ua.cn.stu.multimodule.glue.signin.repositories

import ua.cn.stu.multimodule.core.AuthException
import ua.cn.stu.multimodule.core.unwrapFirst
import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.signin.domain.repositories.ProfileRepository
import javax.inject.Inject

class AdapterProfileRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
) : ProfileRepository {

    override suspend fun canLoadProfile(): Boolean {
        return try {
            accountsDataRepository.getAccount().unwrapFirst()
            true
        } catch (ignored: AuthException) {
            false
        }
    }
}