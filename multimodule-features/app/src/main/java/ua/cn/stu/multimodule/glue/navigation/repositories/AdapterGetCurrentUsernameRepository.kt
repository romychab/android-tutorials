package ua.cn.stu.multimodule.glue.navigation.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.navigation.domain.repositories.GetCurrentUsernameRepository
import javax.inject.Inject

class AdapterGetCurrentUsernameRepository @Inject constructor(
    private val accountsRepository: AccountsDataRepository
) : GetCurrentUsernameRepository {

    override fun getCurrentUsername(): Flow<Container<String>> {
        return accountsRepository.getAccount().map { container ->
            container.map { it.username }
        }
    }

    override fun reload() {
        accountsRepository.reload()
    }

}