package ua.cn.stu.multimodule.profile.domain.repositories

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.profile.domain.entities.Profile

interface ProfileRepository {

    /**
     * Listen for a profile info of the current logged-in user.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getProfile(): Flow<Container<Profile>>

    /**
     * Reload profile info flow returned by [getProfile]
     */
    fun reload()

    /**
     * Update username of the current logged-in user.
     */
    suspend fun editUsername(newUsername: String)

}