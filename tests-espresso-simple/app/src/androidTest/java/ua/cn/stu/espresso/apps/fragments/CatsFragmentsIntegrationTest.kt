package ua.cn.stu.espresso.apps.fragments

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.cn.stu.espresso.R
import ua.cn.stu.espresso.apps.testutils.BaseTest
import ua.cn.stu.espresso.apps.testutils.espresso.*
import ua.cn.stu.espresso.di.RepositoriesModule
import ua.cn.stu.espresso.model.Cat

/**
 * Tests for app with navigation based on fragments.
 *
 * This class contains integration tests for fragments. That's why the
 * navigation is not mocked here and the real main activity is used
 * to start integration tests ([CatsFragmentContainerActivity]).
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(RepositoriesModule::class)
@LargeTest
class CatsFragmentsIntegrationTest : BaseTest() {

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat1.jpg",
        description = "The first cat",
        isFavorite = false
    )
    private val catsFlow = MutableStateFlow(listOf(cat))

    private lateinit var scenario: ActivityScenario<CatsFragmentContainerActivity>

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCats() } returns catsFlow
        every { catsRepository.getCatById(any()) } returns catsFlow.map { it.first() }
        every { catsRepository.toggleIsFavorite(any()) } answers {
            catsFlow.value = catsFlow.value.map { it.copy(isFavorite = !it.isFavorite) }
        }
        scenario = ActivityScenario.launch(CatsFragmentContainerActivity::class.java)
    }

    @After
    fun tearDown() {
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

    @Test
    fun testListTitleInActionBar() {
        assertCatsListTitle()
        clickOnCat()
        assertCatDetailsTitle()
        clickOnGoBack()
        assertCatsListTitle()
    }

    @Test
    fun testNavigateUpButton() {
        clickOnCat()
        clickOnNavigateUp()
        assertCatsListTitle()
    }

    @Test
    fun testHardwareBackButton() {
        clickOnCat()
        Espresso.pressBack()
        assertCatsListTitle()
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
            .check(matches(
                withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
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
                    withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
                )
            ))))
    }

    private fun clickOnToggleFavoriteInDetails() {
        onView(allOf(
                withId(R.id.favoriteImageView),
                not(isDescendantOfA(withId(R.id.catsRecyclerView)))
            ))
            .perform(click())
    }

    private fun assertIsFavoriteFlagInactiveInListScreen() {
        onView(withId(R.id.catsRecyclerView))
            .perform(scrollToPosition(1))
            .check(matches(atPosition(1, hasDescendant(
                allOf(
                    withId(R.id.favoriteImageView),
                    withDrawable(R.drawable.ic_favorite_not, R.color.action)
                )
            ))))
    }

    private fun assertCatsListTitle() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        scenario.onActivity { activity ->
            assertEquals(
                context.getString(R.string.fragment_cats_title),
                activity.supportActionBar?.title
            )
        }
    }

    private fun assertCatDetailsTitle() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        scenario.onActivity { activity ->
            assertEquals(
                context.getString(R.string.fragment_cat_details),
                activity.supportActionBar?.title
            )
        }
    }

    private fun clickOnNavigateUp() {
        onView(withContentDescription(
            androidx.appcompat.R.string.abc_action_bar_up_description
        )).perform(click())
    }

}