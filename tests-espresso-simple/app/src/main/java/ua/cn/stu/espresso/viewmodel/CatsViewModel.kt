package ua.cn.stu.espresso.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.cn.stu.espresso.model.Cat
import ua.cn.stu.espresso.model.CatsRepository
import ua.cn.stu.espresso.viewmodel.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class CatsViewModel @Inject constructor(
    val catsRepository: CatsRepository
) : BaseViewModel() {

    val catsLiveData: LiveData<List<CatListItem>> = liveData()

    init {
        viewModelScope.launch {
            catsRepository.getCats().collectLatest { catsList ->
                catsLiveData.update(mapCats(catsList))
            }
        }
    }

    fun deleteCat(cat: CatListItem.Cat) {
        catsRepository.delete(cat.originCat)
    }

    fun toggleFavorite(cat: CatListItem.Cat) {
        catsRepository.toggleIsFavorite(cat.originCat)
    }

    private fun mapCats(cats: List<Cat>): List<CatListItem> {
        val size = 10
        return cats
            .chunked(size)
            .mapIndexed { index, list ->
                val fromIndex = index * size + 1
                val toIndex = fromIndex + list.size - 1
                val header: CatListItem = CatListItem.Header(index, fromIndex, toIndex)
                listOf(header) + list.map { CatListItem.Cat(it) }
            }
            .flatten()
    }


}