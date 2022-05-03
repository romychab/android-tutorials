package ua.cn.stu.http.app.screens.main.tabs.dashboard

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.screens.base.BaseViewModel
import ua.cn.stu.http.app.utils.MutableLiveEvent
import ua.cn.stu.http.app.utils.publishEvent
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.utils.logger.LogCatLogger
import ua.cn.stu.http.app.Singletons
import ua.cn.stu.http.app.model.Success
import ua.cn.stu.http.app.model.accounts.AccountsRepository
import ua.cn.stu.http.app.model.boxes.BoxesRepository
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.app.utils.logger.Logger

class BoxViewModel(
    private val boxId: Long,
    private val boxesRepository: BoxesRepository = Singletons.boxesRepository,
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger) {

    private val _shouldExitEvent = MutableLiveEvent<Boolean>()
    val shouldExitEvent = _shouldExitEvent.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ONLY_ACTIVE)
                .map { res -> res.map { boxes -> boxes.firstOrNull { it.box.id == boxId } } }
                .collect { res ->
                    _shouldExitEvent.publishEvent(res is Success && res.value == null)
                }
        }
    }
}