package ua.cn.stu.hilt.app.screens.main.tabs.dashboard

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.cn.stu.hilt.app.model.Success
import ua.cn.stu.hilt.app.model.accounts.AccountsRepository
import ua.cn.stu.hilt.app.model.boxes.BoxesRepository
import ua.cn.stu.hilt.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.hilt.app.screens.base.BaseViewModel
import ua.cn.stu.hilt.app.utils.MutableLiveEvent
import ua.cn.stu.hilt.app.utils.logger.Logger
import ua.cn.stu.hilt.app.utils.publishEvent
import ua.cn.stu.hilt.app.utils.share

class BoxViewModel @AssistedInject constructor(
    @Assisted boxId: Long,
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
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

    @AssistedFactory
    interface Factory {
        fun create(boxId: Long): BoxViewModel
    }

}