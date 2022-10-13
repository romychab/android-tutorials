package ua.cn.stu.espresso.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ua.cn.stu.espresso.model.Cat
import ua.cn.stu.espresso.model.CatsRepository
import ua.cn.stu.espresso.viewmodel.base.BaseViewModel


class CatDetailsViewModel @AssistedInject constructor(
    private val catsRepository: CatsRepository,
    @Assisted catId: Long
) : BaseViewModel() {

    val catLiveData: LiveData<Cat> = liveData()

    init {
        viewModelScope.launch {
            catsRepository.getCatById(catId).filterNotNull().collect {
                catLiveData.update(it)
            }
        }
    }

    fun toggleFavorite() {
        val cat = catLiveData.value ?: return
        catsRepository.toggleIsFavorite(cat)
    }

    @AssistedFactory
    interface Factory {
        fun create(catId: Long): CatDetailsViewModel
    }

}