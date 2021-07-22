package ua.cn.stu.mvvmsavedstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _squares = savedStateHandle.getLiveData<Squares>(KEY_SQUARES)
    val squares: LiveData<Squares> = _squares

    init {
        if (!savedStateHandle.contains(KEY_SQUARES)) {
            savedStateHandle[KEY_SQUARES] = createSquares()
        }
    }

    fun generateSquares() {
        _squares.value = createSquares()
    }

    private fun createSquares(): Squares {
        return Squares(
            size = Random.nextInt(5, 11),
            colorProducer = { -Random.nextInt(0xFFFFFF) }
        )
    }


    companion object {
        const val KEY_SQUARES = "squares"
    }
}