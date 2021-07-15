package ua.cn.stu.mvvmnavigation.screens.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.cn.stu.mvvmnavigation.Event
import ua.cn.stu.mvvmnavigation.R
import ua.cn.stu.mvvmnavigation.navigator.Navigator
import ua.cn.stu.mvvmnavigation.screens.base.BaseViewModel
import ua.cn.stu.mvvmnavigation.screens.edit.EditFragment.Screen

class EditViewModel(
    private val navigator: Navigator,
    screen: Screen
) : BaseViewModel() {

    private val _initialMessageEvent = MutableLiveData<Event<String>>()
    val initialMessageEvent: LiveData<Event<String>> = _initialMessageEvent

    init {
        // sending initial value from screen argument to the fragment
        _initialMessageEvent.value = Event(screen.initialValue)
    }

    fun onSavePressed(message: String) {
        if (message.isBlank()) {
            navigator.toast(R.string.empty_message)
            return
        }
        navigator.goBack(message)
    }

    fun onCancelPressed() {
        navigator.goBack()
    }

}