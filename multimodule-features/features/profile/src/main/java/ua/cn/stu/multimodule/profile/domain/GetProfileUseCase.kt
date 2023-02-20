package ua.cn.stu.multimodule.profile.domain

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.profile.domain.entities.Profile
import ua.cn.stu.multimodule.profile.domain.repositories.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    /**
     * Listen for a profile info of the current logged-in user.
     * @return infinite flow, always success; errors are delivered to [Container]
     */
    fun getProfile(): Flow<Container<Profile>> {
        return profileRepository.getProfile()
    }

    /**
     * Try to reload profile flow returned by [getProfile].
     */
    fun reload() {
        profileRepository.reload()
    }

}