package ua.cn.stu.http.app.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ua.cn.stu.http.app.utils.share
import ua.cn.stu.http.app.Singletons
import ua.cn.stu.http.app.model.accounts.AccountsRepository

class MainActivityViewModel(
    private val accountsRepository: AccountsRepository = Singletons.accountsRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username = _username.share()

    init {
        viewModelScope.launch {
            // listening for the current account and send the username to be displayed in the toolbar
            accountsRepository.getAccount().collect { result ->
                _username.value = result.getValueOrNull()?.username?.let { "@$it" } ?: ""
            }
        }
    }

}