package ua.cn.stu.multimodule.core.presentation.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import ua.cn.stu.multimodule.core.presentation.BaseViewModel

/**
 * Internal implementation of [LiveValue] which is used in [BaseViewModel].
 */
internal class MutableLiveValue<T>(
    private val liveData: MutableLiveData<T> = MutableLiveData()
) : LiveValue<T> {

    override fun observe(lifecycleOwner: LifecycleOwner, listener: (T) -> Unit) {
        this.liveData.observe(lifecycleOwner, listener)
    }

    override fun requireValue(): T {
        return liveData.value ?: throw IllegalStateException("Value hasn't been assigned yet")
    }

    override fun getValue(): T? {
        return liveData.value
    }

    fun setValue(value: T) {
        this.liveData.value = value
    }
}