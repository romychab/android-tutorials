package ua.cn.stu.tests.data.settings

import android.content.Context
import android.content.SharedPreferences
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ua.cn.stu.tests.testutils.arranged

class SharedPreferencesAppSettingsTest {

    @get:Rule
    val rule = MockKRule(this)

    @MockK
    lateinit var context: Context

    @MockK
    lateinit var preferences: SharedPreferences

    @RelaxedMockK
    lateinit var editor: SharedPreferences.Editor

    private lateinit var settings: SharedPreferencesAppSettings

    private val expectedTokenKey = "currentToken"

    @Before
    fun setUp() {
        every { context.getSharedPreferences(any(), any()) } returns
                preferences
        every { preferences.edit() } returns editor

        settings = SharedPreferencesAppSettings(context)
    }

    @Test
    fun setCurrentTokenPutsValueToPreferences() {
        arranged()

        settings.setCurrentToken("token")

        verifySequence {
            preferences.edit()
            editor.putString(expectedTokenKey, "token")
            editor.apply()
        }
    }

    @Test
    fun setCurrentTokenWithNullRemovesValueFromPreferences() {
        arranged()

        settings.setCurrentToken(null)

        verifySequence {
            preferences.edit()
            editor.remove(expectedTokenKey)
            editor.apply()
        }
    }

    @Test
    fun getCurrentTokenReturnsValueFromPreferences() {
        every { preferences.getString(any(), any()) } returns "token"

        val token = settings.getCurrentToken()

        assertEquals("token", token)
    }

}