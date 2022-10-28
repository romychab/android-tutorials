package ua.cn.stu.robolectric.viewmodel

import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.cn.stu.robolectric.model.Cat
import ua.cn.stu.robolectric.model.CatsRepository
import ua.cn.stu.robolectric.testutils.base.BaseTest

class CatsViewModelTest : BaseTest() {

    @RelaxedMockK
    lateinit var catsRepository: CatsRepository

    private lateinit var viewModel: CatsViewModel

    private val catsFlow = MutableStateFlow<List<Cat>>(emptyList())

    private val cat = Cat(
        id = 1,
        name = "Cat",
        photoUrl = "url",
        description = "desc",
        isFavorite = false
    )
    private val anotherCat = Cat(
        id = 2,
        name = "Cat2",
        photoUrl = "url2",
        description = "desc2",
        isFavorite = true
    )

    @Before
    fun setUp() {
        every { catsRepository.getCats() } returns catsFlow
        viewModel = CatsViewModel(catsRepository)
    }

    @Test
    fun deleteCat_callsDelete() {
        val catListItem = CatListItem.Cat(cat)

        viewModel.deleteCat(catListItem)

        verify {
            catsRepository.delete(cat)
        }
    }

    @Test
    fun toggleFavorite_callsToggleFavorite() {
        val catListItem = CatListItem.Cat(cat)

        viewModel.toggleFavorite(catListItem)

        verify {
            catsRepository.toggleIsFavorite(cat)
        }
    }

    @Test
    fun init_collectsCatList() {
        catsFlow.value = listOf(cat, anotherCat)

        val listItems = viewModel.catsLiveData.value

        assertEquals(
            listOf(
                CatListItem.Header(0, 1, 2),
                CatListItem.Cat(cat),
                CatListItem.Cat(anotherCat)
            ),
            listItems
        )
    }

    @Test
    fun init_placesHeadersCorrectly() {
        catsFlow.value = List(27) { cat }

        val listItems = viewModel.catsLiveData.value!!

        assertEquals(1, (listItems[0] as CatListItem.Header).fromIndex)
        assertEquals(10, (listItems[0] as CatListItem.Header).toIndex)

        assertEquals(11, (listItems[11] as CatListItem.Header).fromIndex)
        assertEquals(20, (listItems[11] as CatListItem.Header).toIndex)

        assertEquals(21, (listItems[22] as CatListItem.Header).fromIndex)
        assertEquals(27, (listItems[22] as CatListItem.Header).toIndex)
    }

}