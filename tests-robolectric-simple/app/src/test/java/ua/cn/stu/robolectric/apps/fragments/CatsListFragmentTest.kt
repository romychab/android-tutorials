package ua.cn.stu.robolectric.apps.fragments

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ua.cn.stu.robolectric.R
import ua.cn.stu.robolectric.apps.fragments.di.FragmentRouterModule
import ua.cn.stu.robolectric.testutils.*
import ua.cn.stu.robolectric.testutils.rules.ImmediateDiffUtilRule
import ua.cn.stu.robolectric.di.RepositoriesModule
import ua.cn.stu.robolectric.model.Cat
import ua.cn.stu.robolectric.testutils.base.BaseRobolectricTest
import ua.cn.stu.robolectric.testutils.extensions.containsDrawable
import ua.cn.stu.robolectric.testutils.extensions.requireViewHolderAt
import ua.cn.stu.robolectric.testutils.extensions.with
import ua.cn.stu.robolectric.testutils.imageloader.FakeImageLoader

@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@UninstallModules(RepositoriesModule::class, FragmentRouterModule::class)
class CatsListFragmentTest : BaseRobolectricTest() {

    @get:Rule
    val immediateDiffUtilRule = ImmediateDiffUtilRule()

    @BindValue @RelaxedMockK
    lateinit var router: FragmentRouter

    private val cat1 = Cat(
        id = 1,
        name = "Lucky",
        photoUrl = "cat1.jpg",
        description = "The first cat",
        isFavorite = false
    )
    private val cat2 = Cat(
        id = 2,
        name = "Tiger",
        photoUrl = "cat2.jpg",
        description = "The second cat",
        isFavorite = true
    )
    private val catsFlow = MutableStateFlow(listOf(cat1, cat2))

    private lateinit var scenario: ActivityScenario<*>

    @Before
    override fun setUp() {
        super.setUp()
        every { catsRepository.getCats() } returns catsFlow
        scenario = launchHiltFragment<CatsListFragment>()
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun catsAndHeadersAreDisplayedInList() = scenario.with {
        val recyclerView = findViewById<RecyclerView>(R.id.catsRecyclerView)

        val headerHolder = recyclerView.requireViewHolderAt(0)
        val firstCatHolder = recyclerView.requireViewHolderAt(1)
        val secondCatHolder = recyclerView.requireViewHolderAt(2)

        // assert total items count (1 header + 2 cats = 3)
        assertEquals(3, recyclerView.adapter!!.itemCount)
        // assert header
        assertEquals("Cats: 1 … 2", (headerHolder.itemView as TextView).text)
        // assert the first cat
        with(firstCatHolder.itemView) {
            assertEquals("Lucky", findViewById<TextView>(R.id.catNameTextView).text)
            assertEquals("The first cat", findViewById<TextView>(R.id.catDescriptionTextView).text)
            assertTrue(
                findViewById<ImageView>(R.id.favoriteImageView)
                    .containsDrawable(R.drawable.ic_favorite_not, R.color.action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.deleteImageView)
                    .containsDrawable(R.drawable.ic_delete, R.color.action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.catImageView)
                    .containsDrawable(FakeImageLoader.createDrawable("cat1.jpg"))
            )
        }
        // assert the second cat
        with(secondCatHolder.itemView) {
            assertEquals("Tiger", findViewById<TextView>(R.id.catNameTextView).text)
            assertEquals("The second cat", findViewById<TextView>(R.id.catDescriptionTextView).text)
            assertTrue(
                findViewById<ImageView>(R.id.favoriteImageView)
                    .containsDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.deleteImageView)
                    .containsDrawable(R.drawable.ic_delete, R.color.action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.catImageView)
                    .containsDrawable(FakeImageLoader.createDrawable("cat2.jpg"))
            )
        }
    }

    @Test
    fun clickOnCatLaunchesDetails() = scenario.with {
        val firstCatHolder = getFirstHolderFromCatsList()
        firstCatHolder.itemView.performClick()

        verify {
            router.showDetails(1L)
        }
    }

    @Test
    fun clickOnFavoriteTogglesFlag() = scenario.with {
        // arrange
        every { catsRepository.toggleIsFavorite(any()) } answers {
            val cat = firstArg<Cat>()
            catsFlow.value = listOf(
                cat.copy(isFavorite = !cat.isFavorite),
                cat2
            )
        }

        // act 1 - turn on a favorite flag
        getFirstHolderFromCatsList().itemView
            .findViewById<View>(R.id.favoriteImageView).performClick()

        // assert 1
        assertFavorite(R.drawable.ic_favorite, R.color.highlighted_action)

        // act 2 - turn off a favorite flag
        getFirstHolderFromCatsList().itemView
            .findViewById<View>(R.id.favoriteImageView).performClick()

        // assert 2
        assertFavorite(R.drawable.ic_favorite_not, R.color.action)
    }

    @Test
    fun clickOnDeleteRemovesCatFromList() = scenario.with {
        val recyclerView = findViewById<RecyclerView>(R.id.catsRecyclerView)
        every { catsRepository.delete(any()) } answers {
            catsFlow.value = listOf(cat2)
        }

        getFirstHolderFromCatsList().itemView
            .findViewById<View>(R.id.deleteImageView).performClick()
        Robolectric.flushForegroundThreadScheduler()

        // assert
        recyclerView.scrollToPosition(0)
        val headerHolder = recyclerView.findViewHolderForAdapterPosition(0)!!
        recyclerView.scrollToPosition(1)
        val secondCatHolder = recyclerView.findViewHolderForAdapterPosition(1)!!
        // assert total items count (1 header + 1 cat = 2)
        assertEquals(2, recyclerView.adapter!!.itemCount)
        // assert header
        assertEquals("Cats: 1 … 1", (headerHolder.itemView as TextView).text)
        // assert cat item
        with(secondCatHolder.itemView) {
            assertEquals("Tiger", findViewById<TextView>(R.id.catNameTextView).text)
            assertEquals("The second cat", findViewById<TextView>(R.id.catDescriptionTextView).text)
            assertTrue(
                findViewById<ImageView>(R.id.favoriteImageView)
                    .containsDrawable(R.drawable.ic_favorite, R.color.highlighted_action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.deleteImageView)
                    .containsDrawable(R.drawable.ic_delete, R.color.action)
            )
            assertTrue(
                findViewById<ImageView>(R.id.catImageView)
                    .containsDrawable(FakeImageLoader.createDrawable("cat2.jpg"))
            )
        }
    }

    private fun Activity.assertFavorite(expectedDrawableRes: Int, expectedTintColorRes: Int? = null) {
        with(getFirstHolderFromCatsList().itemView) {
            val favoriteImageView = findViewById<ImageView>(R.id.favoriteImageView)
            assertTrue(
                favoriteImageView.containsDrawable(expectedDrawableRes, expectedTintColorRes)
            )
        }
    }

    private fun Activity.getFirstHolderFromCatsList(): RecyclerView.ViewHolder {
        val recyclerView = findViewById<RecyclerView>(R.id.catsRecyclerView)
        return recyclerView.requireViewHolderAt(1)
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