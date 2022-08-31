package ua.cn.stu.tests.presentation.main.tabs.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.cn.stu.tests.domain.Result
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.domain.boxes.BoxesRepository
import ua.cn.stu.tests.domain.boxes.entities.Box
import ua.cn.stu.tests.domain.boxes.entities.BoxesFilter
import ua.cn.stu.tests.presentation.base.BaseViewModel
import ua.cn.stu.tests.utils.logger.Logger
import ua.cn.stu.tests.utils.share
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger) {

    private val _boxes = MutableLiveData<Result<List<Box>>>()
    val boxes = _boxes.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE).collect { result ->
                _boxes.value = result.map { list ->
                    list.map { it.box }
                }
            }
        }
    }

    fun reload() = viewModelScope.launch {
        boxesRepository.reload(BoxesFilter.ONLY_ACTIVE)
    }

}