package ua.cn.stu.http.app.screens.main.tabs.settings

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
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.app.utils.logger.Logger

class SettingsViewModel(
    private val boxesRepository: BoxesRepository = Singletons.boxesRepository,
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
) : BaseViewModel(accountsRepository, logger), SettingsAdapter.Listener {

    private val _boxSettings = MutableLiveData<Result<List<BoxAndSettings>>>()
    val boxSettings = _boxSettings.share()

    init {
        viewModelScope.launch {
            boxesRepository.getBoxesAndSettings(BoxesFilter.ALL).collect {
                _boxSettings.value = it
            }
        }
    }

    fun tryAgain() = viewModelScope.safeLaunch {
        boxesRepository.reload(BoxesFilter.ALL)
    }

    override fun enableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.activateBox(box)
    }

    override fun disableBox(box: Box) = viewModelScope.safeLaunch {
        boxesRepository.deactivateBox(box)
    }

}