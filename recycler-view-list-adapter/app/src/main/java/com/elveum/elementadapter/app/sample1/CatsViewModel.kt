package com.elveum.elementadapter.app.sample1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elveum.elementadapter.app.model.Cat
import com.elveum.elementadapter.app.model.CatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    private val catsRepository: CatsRepository
) : ViewModel() {

    private val _catsLiveData = MutableLiveData<List<Cat>>()
    val catsLiveData: LiveData<List<Cat>> = _catsLiveData

    init {
        viewModelScope.launch {
            catsRepository.getCats().collectLatest { catsList ->
                _catsLiveData.value = catsList
            }
        }
    }

    fun deleteCat(cat: Cat) {
        catsRepository.delete(cat)
    }

    fun toggleFavorite(cat: Cat) {
        catsRepository.toggleIsFavorite(cat)
    }

}