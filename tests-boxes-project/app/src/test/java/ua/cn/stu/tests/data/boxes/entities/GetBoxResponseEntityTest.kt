package ua.cn.stu.tests.data.boxes.entities

import android.graphics.Color
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import ua.cn.stu.tests.domain.boxes.entities.Box
import ua.cn.stu.tests.domain.boxes.entities.BoxAndSettings

class GetBoxResponseEntityTest {

    @Before
    fun setUp() {
        mockkStatic(Color::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(Color::class)
    }

    @Test
    fun toBoxAndSettingsMapsToInAppEntity() {
        val responseEntity = GetBoxResponseEntity(
            id = 2,
            colorName = "Red",
            colorValue = "#ff0000",
            isActive = true
        )
        every { Color.parseColor(any()) } returns Color.RED

        val inAppEntity = responseEntity.toBoxAndSettings()

        val expectedInAppEntity = BoxAndSettings(
            box = Box(id = 2, colorName = "Red", colorValue = Color.RED),
            isActive = true
        )
        Assert.assertEquals(expectedInAppEntity, inAppEntity)
    }
}