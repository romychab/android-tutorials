package ua.cn.stu.tests.presentation.main.tabs.dashboard

import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ua.cn.stu.tests.domain.Pending
import ua.cn.stu.tests.domain.Result
import ua.cn.stu.tests.domain.Success
import ua.cn.stu.tests.domain.boxes.BoxesRepository
import ua.cn.stu.tests.domain.boxes.entities.BoxAndSettings
import ua.cn.stu.tests.domain.boxes.entities.BoxesFilter
import ua.cn.stu.tests.utils.requireValue
import ua.cn.stu.tests.testutils.ViewModelTest
import ua.cn.stu.tests.testutils.createBoxAndSettings

class BoxViewModelTest : ViewModelTest() {

    lateinit var flow: MutableStateFlow<Result<List<BoxAndSettings>>>

    @MockK
    lateinit var boxesRepository: BoxesRepository

    lateinit var viewModel: BoxViewModel

    private val boxId = 1L
    private val anotherBoxId = 2L

    @Before
    fun setUp() {
        flow = MutableStateFlow(Pending())
        every {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
        } returns flow
        viewModel = BoxViewModel(boxId, boxesRepository, accountsRepository, logger)

    }

    @Test
    fun shouldExitEventIsFiredAfterDisablingBox() {
        val listWithBox = listOf(
            createBoxAndSettings(id = boxId)
        )
        val listWithoutBox = listOf(
            createBoxAndSettings(id = anotherBoxId)
        )

        flow.value = Success(listWithBox)
        val shouldExitEvent1 = viewModel.shouldExitEvent.requireValue().get()!!
        flow.value = Success(listWithoutBox)
        val shouldExitEvent2 = viewModel.shouldExitEvent.requireValue().get()!!

        assertFalse(shouldExitEvent1)
        assertTrue(shouldExitEvent2)
    }

}