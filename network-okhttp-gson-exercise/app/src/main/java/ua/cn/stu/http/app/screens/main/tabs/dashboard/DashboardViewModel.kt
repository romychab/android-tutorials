package ua.cn.stu.http.app.screens.main.tabs.dashboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.screens.base.BaseViewModel
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.utils.logger.LogCatLogger
import ua.cn.stu.http.app.Singletons
import ua.cn.stu.http.app.model.Result
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.model.boxes.BoxesRepository
import ua.cn.stu.http.app.model.boxes.entities.Box
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.app.utils.logger.Logger

class DashboardViewModel(
    private val boxesRepository: BoxesRepository = Singletons.boxesRepository,
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _boxes = MutableLiveData<Result<List<Box>>>()
    val boxes = _boxes.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE).collect { result ->
                _boxes.value = result.map { list -> list.map { it.box } }
            }
        }
    }

    fun reload() = viewModelScope.launch {
        boxesRepository.reload(BoxesFilter.ONLY_ACTIVE)
    }

}