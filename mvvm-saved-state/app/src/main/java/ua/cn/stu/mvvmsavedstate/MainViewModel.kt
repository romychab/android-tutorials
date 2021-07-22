package ua.cn.stu.mvvmsavedstate

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Here the example differs a bit from the example in video tutorial.

    // Usually it's better not to save complex objects in Bundle (and SavedStateHandle too).
    // For example you can save a simple Long value named SEED; and then use it for random
    // generator initialization which always generates the same squares for the same seed.

    private val _seed = savedStateHandle.getLiveData<Long>(KEY_SEED)
    val squares: LiveData<Squares> = Transformations.map(_seed) { createSquares(it) }

    init {
        if (!savedStateHandle.contains(KEY_SEED)) {
            savedStateHandle[KEY_SEED] = Random.nextLong()
        }
    }

    fun generateSquares() {
        _seed.value = Random.nextLong()
    }

    private fun createSquares(seed: Long): Squares {
        // create Random object with the specified seed
        // Random generators initialized with the same seed will always produce the same sequence of numbers.
        val random = Random(seed)
        return Squares(
            size = random.nextInt(5, 11),
            colorProducer = { -random.nextInt(0xFFFFFF) }
        )
    }

    companion object {
        const val KEY_SEED = "seed"
    }
}