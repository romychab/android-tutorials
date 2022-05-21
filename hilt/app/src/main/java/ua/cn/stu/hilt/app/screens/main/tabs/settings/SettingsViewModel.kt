package ua.cn.stu.hilt.app.screens.main.tabs.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.cn.stu.hilt.app.model.Result
import ua.cn.stu.hilt.app.model.accounts.AccountsRepository
import ua.cn.stu.hilt.app.model.boxes.BoxesRepository
import ua.cn.stu.hilt.app.model.boxes.entities.Box
import ua.cn.stu.hilt.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.hilt.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.hilt.app.screens.base.BaseViewModel
import ua.cn.stu.hilt.app.utils.logger.Logger
import ua.cn.stu.hilt.app.utils.share
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val boxesRepository: BoxesRepository,
    accountsRepository: AccountsRepository,
    logger: Logger
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