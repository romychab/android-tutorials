package ua.cn.stu.multimodule.data.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SharedPreferencesSettingsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsDataSource, OnSharedPreferenceChangeListener {

    private val preferences = context.getSharedPreferences(
        PREFERENCES_NAME, Context.MODE_PRIVATE
    )
    private val tokenFlow = MutableStateFlow<String?>(null)

    init {
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun setToken(token: String?) {
        preferences.edit {
            if (token == null) {
                remove(PREF_TOKEN)
            } else {
                putString(PREF_TOKEN, token)
            }
        }
    }

    override fun getToken(): String? {
        return preferences.getString(PREF_TOKEN, null)
    }

    override fun listenToken(): Flow<String?> {
        return tokenFlow
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        tokenFlow.value = getToken()
    }

    private companion object {
        const val PREFERENCES_NAME = "preferences"
        const val PREF_TOKEN = "token"
    }
}