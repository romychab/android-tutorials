package ua.cn.stu.multimodule.glue.signin.repositories

import ua.cn.stu.multimodule.data.settings.SettingsDataSource
import ua.cn.stu.multimodule.signin.domain.repositories.AuthTokenRepository
import javax.inject.Inject

class AdapterAuthTokenRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
) : AuthTokenRepository {

    override suspend fun setToken(token: String?) {
        settingsDataSource.setToken(token)
    }

    override suspend fun getToken(): String? {
        return settingsDataSource.getToken()
    }
}