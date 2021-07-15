package ua.cn.stu.mvvmnavigation.screens.hello

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ua.cn.stu.mvvmnavigation.R
import ua.cn.stu.mvvmnavigation.navigator.Navigator
import ua.cn.stu.mvvmnavigation.screens.base.BaseViewModel
import ua.cn.stu.mvvmnavigation.screens.edit.EditFragment
import ua.cn.stu.mvvmnavigation.screens.hello.HelloFragment.Screen

class HelloViewModel(
    private val navigator: Navigator,
    screen: Screen
) : BaseViewModel() {

    private val _currentMessageLiveData = MutableLiveData<String>()
    val currentMessageLiveData: LiveData<String> = _currentMessageLiveData

    init {
        _currentMessageLiveData.value = navigator.getString(R.string.hello_world)
    }

    override fun onResult(result: Any) {
        if (result is String) {
            _currentMessageLiveData.value = result
        }
    }

    fun onEditPressed() {
        navigator.launch(EditFragment.Screen(initialValue = currentMessageLiveData.value!!))
    }
}