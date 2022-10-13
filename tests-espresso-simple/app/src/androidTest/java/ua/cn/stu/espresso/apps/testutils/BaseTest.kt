package ua.cn.stu.espresso.apps.testutils

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.junit4.MockKRule
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import ua.cn.stu.espresso.apps.testutils.rules.FakeImageLoaderRule
import ua.cn.stu.espresso.apps.testutils.rules.TestViewModelScopeRule
import ua.cn.stu.espresso.di.RepositoriesModule
import ua.cn.stu.espresso.model.CatsRepository
import javax.inject.Inject

/**
 * Base class for all UI tests.
 */
open class BaseTest {

    @get:Rule
    val testViewModelScopeRule = TestViewModelScopeRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

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