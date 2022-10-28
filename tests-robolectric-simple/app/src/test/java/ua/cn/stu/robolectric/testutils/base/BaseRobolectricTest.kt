package ua.cn.stu.robolectric.testutils.base

import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import ua.cn.stu.robolectric.model.CatsRepository
import ua.cn.stu.robolectric.testutils.rules.FakeImageLoaderRule
import javax.inject.Inject

open class BaseRobolectricTest : BaseTest() {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val fakeImageLoaderRule = FakeImageLoaderRule()

    @Inject
    lateinit var catsRepository: CatsRepository

    @Before
    open fun setUp() {
        hiltRule.inject()
    }

}