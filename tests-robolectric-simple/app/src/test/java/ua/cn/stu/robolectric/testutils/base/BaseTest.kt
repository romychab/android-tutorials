package ua.cn.stu.robolectric.testutils.base

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.junit4.MockKRule
import org.junit.Rule
import ua.cn.stu.robolectric.testutils.rules.TestDispatcherRule

open class BaseTest {

    @get:Rule
    val testDispatcherRule = TestDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockkRule = MockKRule(this)

}