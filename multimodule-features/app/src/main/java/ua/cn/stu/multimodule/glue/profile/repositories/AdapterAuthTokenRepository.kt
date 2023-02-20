package ua.cn.stu.multimodule.glue.profile.repositories

import ua.cn.stu.multimodule.data.settings.SettingsDataSource
import ua.cn.stu.multimodule.profile.domain.repositories.AuthTokenRepository
import javax.inject.Inject

class AdapterAuthTokenRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : AuthTokenRepository {

    override suspend fun clearToken() {
        settingsDataSource.setToken(null)
    }

}