package ua.cn.stu.espresso.apps.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.cn.stu.espresso.R
import ua.cn.stu.espresso.apps.testutils.BaseTest
import ua.cn.stu.espresso.apps.testutils.espresso.*
import ua.cn.stu.espresso.di.RepositoriesModule
import ua.cn.stu.espresso.model.Cat

/**
 * Tests for app with navigation based on activities.
 *
 * This class contains integration tests so it launches the first
 * main activity of the application - [CatsListActivity] and then
 * it tests the navigation and data consistency between screens.
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(RepositoriesModule::class)
@LargeTest
class CatsActivitiesIntegrationTest : BaseTest() {

    private lateinit var scenario: ActivityScenario<CatsListActivity>

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat1.jpg",
        description = "The first cat",
        isFavorite = false
    )
    private val catsFlow = MutableStateFlow(listOf(cat))

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCats() } returns catsFlow
        every { catsRepository.getCatById(any()) } returns catsFlow.map { it.first() }
        every { catsRepository.toggleIsFavorite(any()) } answers {
            catsFlow.value = catsFlow.value.map { it.copy(isFavorite = !it.isFavorite) }
        }
        Intents.init()
        scenario = ActivityScenario.launch(CatsListActivity::class.java)
    }

    @After
    fun tearDown() {
        Intents.release()
        scenario.close()
    }

    @Test
    fun testFavoriteFlag() {
        clickOnToggleFavoriteInListScreen()
        clickOnCat()
        assertIsFavoriteFlagActiveInDetailsScreen()
        clickOnGoBack()
        assertIsFavoriteFlagActiveInListScreen()
        clickOnCat()
        clickOnToggleFavoriteInDetails()
        clickOnGoBack()
        assertIsFavoriteFlagInactiveInListScreen()
    }

    private fun clickOnToggleFavoriteInListScreen() {
        onView(withId(R.id.catsRecyclerView))
            .perform(actionOnItemAtPosition(1, clickOnView(R.id.favoriteImageView)))
    }

    private fun clickOnCat() {
        onView(withId(R.id.catsRecyclerView))
            .perform(actionOnItemAtPosition(1, click()))
    }

    private fun assertIsFavoriteFlagActiveInDetailsScreen() {
        onView(allOf(withId(R.id.favoriteImageView), not(isDescendantOfA(withId(R.id.catsRecyclerView)))))
            .check(matches(withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
    }

    private fun clickOnGoBack() {
        onView(withId(R.id.goBackButton)).perform(click())
    }

    private fun assertIsFavoriteFlagActiveInListScreen() {
        onView(withId(R.id.catsRecyclerView))
            .perform(scrollToPosition(1))
            .check(matches(atPosition(1, hasDescendant(
                allOf(
                    withId(R.id.favoriteImageView),
                    withDrawable(R.drawable.ic_favorite, R.color.highlighted_action))
                )
            )))
    }

    private fun clickOnToggleFavoriteInDetails() {
        onView(allOf(withId(R.id.favoriteImageView), not(isDescendantOfA(withId(R.id.catsRecyclerView)))))
            .perform(click())
    }

    private fun assertIsFavoriteFlagInactiveInListScreen() {
        onView(withId(R.id.catsRecyclerView))
            .perform(scrollToPosition(1))
            .check(matches(atPosition(1, hasDescendant(
                allOf(
                    withId(R.id.favoriteImageView),
                    withDrawable(R.drawable.ic_favorite_not, R.color.action))
                )
            )))
    }

}
