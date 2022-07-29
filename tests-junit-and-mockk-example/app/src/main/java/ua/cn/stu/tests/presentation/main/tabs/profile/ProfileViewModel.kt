package ua.cn.stu.tests.presentation.main.tabs.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.cn.stu.tests.domain.Result
import ua.cn.stu.tests.domain.accounts.AccountsRepository
import ua.cn.stu.tests.domain.accounts.entities.Account
import ua.cn.stu.tests.presentation.base.BaseViewModel
import ua.cn.stu.tests.utils.logger.Logger
import ua.cn.stu.tests.utils.share
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    accountsRepository: AccountsRepository,
    logger: Logger
) : BaseViewModel(accountsRepository, logger) {

    private val _account = MutableLiveData<Result<Account>>()
    val account = _account.share()

    init {
        viewModelScope.launch {
            accountsRepository.getAccount().collect {
                _account.value = it
            }
        }
    }

    fun reload() {
        accountsRepository.reloadAccount()
    }

}