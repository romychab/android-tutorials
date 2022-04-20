package ua.cn.stu.http.app.screens.main.tabs.profile

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
import ua.cn.stu.http.app.model.accounts.entities.Account
import ua.cn.stu.http.app.utils.logger.Logger

class ProfileViewModel(
    accountsRepository: AccountsRepository = Singletons.accountsRepository,
    logger: Logger = LogCatLogger
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