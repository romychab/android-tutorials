package ua.cn.stu.espresso.apps.navcomponent

import androidx.navigation.NavController
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.cn.stu.espresso.R
import ua.cn.stu.espresso.apps.testutils.BaseTest
import ua.cn.stu.espresso.apps.testutils.FakeImageLoader
import ua.cn.stu.espresso.apps.testutils.espresso.withDrawable
import ua.cn.stu.espresso.apps.testutils.launchNavHiltFragment
import ua.cn.stu.espresso.di.RepositoriesModule
import ua.cn.stu.espresso.model.Cat

/**
 * Tests for app with navigation based on Nav Component.
 *
 * This class contains tests for [NavCatDetailsFragment]. Real navigation
 * is replaced by a fake [NavController] created by MockK.
 * The fragment itself is launched in a separate empty activity container by
 * using [launchNavHiltFragment] method.
 */
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@UninstallModules(RepositoriesModule::class)
@MediumTest
class NavCatDetailsFragmentTest : BaseTest() {

    @RelaxedMockK
    lateinit var navController: NavController

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat.jpg",
        description = "Meow-meow",
        isFavorite = true
    )

    private val catsFlow = MutableStateFlow(cat)

    private lateinit var scenario: AutoCloseable

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCatById(any()) } returns catsFlow
        val args = NavCatDetailsFragmentArgs(catId = 1)
        scenario = launchNavHiltFragment<NavCatDetailsFragment>(navController, args.toBundle())
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun catIsDisplayed() {
        // assert
        onView(withId(R.id.catNameTextView))
            .check(matches(ViewMatchers.withText("Lucky")))
        onView(withId(R.id.catDescriptionTextView))
            .check(matches(ViewMatchers.withText("Meow-meow")))
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
        onView(withId(R.id.catImageView))
            .check(matches(withDrawable(FakeImageLoader.createDrawable(cat.photoUrl))))
    }

    @Test
    fun toggleFavoriteTogglesFlag() {
        // arrange
        every { catsRepository.toggleIsFavorite(any()) } answers {
            val cat = firstArg<Cat>()
            val newCat = cat.copy(isFavorite = !cat.isFavorite)
            catsFlow.value = newCat
        }

        // act 1 - turn off favorite flag
        onView(withId(R.id.favoriteImageView)).perform(click())
        // assert 1
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite_not, R.color.action)))

        // act 2 - turn on favorite flag
        onView(withId(R.id.favoriteImageView)).perform(click())
        // assert 2
        onView(withId(R.id.favoriteImageView))
            .check(matches(withDrawable(R.drawable.ic_favorite, R.color.highlighted_action)))
    }

    @Test
    fun clickOnBackFinishesActivity() {
        onView(withId(R.id.goBackButton)).perform(click())

        verify {
            navController.popBackStack()
        }
    }

}