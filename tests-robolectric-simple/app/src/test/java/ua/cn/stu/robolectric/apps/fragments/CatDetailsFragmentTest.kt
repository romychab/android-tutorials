package ua.cn.stu.robolectric.apps.fragments

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.test.core.app.ActivityScenario
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dagger.hilt.android.testing.UninstallModules
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ua.cn.stu.robolectric.R
import ua.cn.stu.robolectric.apps.fragments.di.FragmentRouterModule
import ua.cn.stu.robolectric.testutils.*
import ua.cn.stu.robolectric.di.RepositoriesModule
import ua.cn.stu.robolectric.model.Cat
import ua.cn.stu.robolectric.testutils.base.BaseRobolectricTest
import ua.cn.stu.robolectric.testutils.extensions.containsDrawable
import ua.cn.stu.robolectric.testutils.extensions.with
import ua.cn.stu.robolectric.testutils.imageloader.FakeImageLoader

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@UninstallModules(RepositoriesModule::class, FragmentRouterModule::class)
class CatDetailsFragmentTest : BaseRobolectricTest() {

    @BindValue @RelaxedMockK
    lateinit var router: FragmentRouter

    private val cat = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat.jpg",
        description = "Meow-meow",
        isFavorite = true
    )

    private val catFlow = MutableStateFlow(cat)

    private lateinit var scenario: ActivityScenario<*>

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCatById(any()) } returns catFlow
        scenario = launchHiltFragment {
            CatDetailsFragment.newInstance(cat.id)
        }
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun catIsDisplayed() = scenario.with {
        Assert.assertEquals(
            "Lucky",
            findViewById<TextView>(R.id.catNameTextView).text
        )
        Assert.assertEquals(
            "Meow-meow",
            findViewById<TextView>(R.id.catDescriptionTextView).text
        )
        Assert.assertTrue(
            findViewById<ImageView>(R.id.favoriteImageView)
                .containsDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
        )
        Assert.assertTrue(
            findViewById<ImageView>(R.id.catImageView)
                .containsDrawable(FakeImageLoader.createDrawable(cat.photoUrl))
        )
    }

    @Test
    fun toggleFavoriteTogglesFlag() = scenario.with {
        // arrange
        every { catsRepository.toggleIsFavorite(any()) } answers {
            val cat = firstArg<Cat>()
            val newCat = cat.copy(isFavorite = !cat.isFavorite)
            catFlow.value = newCat
        }
        val favoriteImageView = findViewById<ImageView>(R.id.favoriteImageView)

        // act 1 - turn off favorite flag
        favoriteImageView.performClick()
        // assert 1
        Assert.assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite_not,
                R.color.action
            )
        )

        // act 2 - turn on favorite flag
        favoriteImageView.performClick()
        // assert 2
        Assert.assertTrue(
            favoriteImageView.containsDrawable(
                R.drawable.ic_favorite,
                R.color.highlighted_action
            )
        )
    }

    @Test
    fun clickOnBackFinishesActivity() = scenario.with {
        findViewById<View>(R.id.goBackButton).performClick()

        verify {
            router.goBack()
        }
    }

    /*@Module
    @InstallIn(SingletonComponent::class)
    class FakeFragmentRouterModule {
        @Provides
        @Singleton
        fun bindRouter(): FragmentRouter {
            return mockk(relaxed = true)
        }
    }*/

}