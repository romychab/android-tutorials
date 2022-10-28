package ua.cn.stu.robolectric.model

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ua.cn.stu.robolectric.testutils.base.BaseTest
import ua.cn.stu.robolectric.testutils.runFlowTest

@ExperimentalCoroutinesApi
class CatsRepositoryTest : BaseTest() {

    private lateinit var repository: CatsRepository

    @Before
    fun setUp() {
        repository = CatsRepository()
    }

    @Test
    fun getCatById_emitsCat() = runTest {
        val expectedCat = repository.getCats().first().random()

        val actualCat = repository.getCatById(expectedCat.id).first()

        assertEquals(expectedCat, actualCat)
    }

    @Test
    fun getCatById_withInvalidId_emitsNull() = runTest {
        val cat = repository.getCatById(99999).firstOrNull()
        assertNull(cat)
    }

    @Test
    fun getCatById_afterDelete_emitsNull() = runFlowTest {
        val cat = repository.getCats().first().random()
        val catFlow = repository.getCatById(cat.id)

        val collectedCats = catFlow.startCollecting()
        repository.delete(cat)

        assertEquals(
            listOf(cat, null),
            collectedCats
        )
    }

    @Test
    fun getCatById_afterToggleFavorite_emitsUpdatedCat() = runFlowTest {
        val cat = repository.getCats().first().random()
        val catFlow = repository.getCatById(cat.id)

        val collectedCats = catFlow.startCollecting()
        repository.toggleIsFavorite(cat)

        val expectedUpdatedCat = cat.copy(isFavorite = !cat.isFavorite)
        assertEquals(
            listOf(cat, expectedUpdatedCat),
            collectedCats
        )
    }

    @Test
    fun getCats_afterDelete_emitsUpdatedList() = runFlowTest {
        val cat = repository.getCats().first().random()
        val catsFlow = repository.getCats()

        val collectedCats = catsFlow.startCollecting()
        repository.delete(cat)

        assertEquals(2, collectedCats.size)
        assertTrue(collectedCats[0].contains(cat))
        assertFalse(collectedCats[1].contains(cat))
    }

    @Test
    fun getCats_afterToggleFavorite_emitsUpdatedList() = runFlowTest {
        val cat = repository.getCats().first().random()
        val catsFlow = repository.getCats()

        val collectedCats = catsFlow.startCollecting()
        repository.toggleIsFavorite(cat)

        assertEquals(2, collectedCats.size)
        assertEquals(cat.isFavorite, collectedCats[0].first { it.id == cat.id }.isFavorite)
        assertEquals(!cat.isFavorite, collectedCats[1].first { it.id == cat.id}.isFavorite)
    }
}