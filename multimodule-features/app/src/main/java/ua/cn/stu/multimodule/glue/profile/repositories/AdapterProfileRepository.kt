package ua.cn.stu.multimodule.glue.profile.repositories

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.data.AccountsDataRepository
import ua.cn.stu.multimodule.formatters.DateTimeFormatter
import ua.cn.stu.multimodule.profile.domain.entities.Profile
import ua.cn.stu.multimodule.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class AdapterProfileRepository @Inject constructor(
    private val accountsDataRepository: AccountsDataRepository,
    private val dateTimeFormatter: DateTimeFormatter,
) : ProfileRepository {

    override fun getProfile(): Flow<Container<Profile>> {
        return accountsDataRepository.getAccount().map { container ->
            container.map {
                Profile(
                    id = it.id,
                    username = it.username,
                    email = it.email,
                    createdAtString = dateTimeFormatter.formatDateTime(it.createdAtMillis)
                )
            }
        }
    }

    override fun reload() {
        accountsDataRepository.reload()
    }

    override suspend fun editUsername(newUsername: String) {
        accountsDataRepository.setAccountUsername(newUsername)
    }

}