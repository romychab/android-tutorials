package ua.cn.stu.espresso.viewmodel.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {

    protected fun <T> LiveData<T>.update(value: T) {
        (this as MutableLiveData<T>).value = value
    }

    protected fun <T> liveData(value: T? = null): LiveData<T> {
        return if (value == null) {
            MutableLiveData()
        } else {
            MutableLiveData(value)
        }
    }

}