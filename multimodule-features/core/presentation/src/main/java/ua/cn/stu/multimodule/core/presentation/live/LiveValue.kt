package ua.cn.stu.multimodule.core.presentation.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import ua.cn.stu.multimodule.core.presentation.BaseViewModel

/**
 * Wrapper for value that can be observed with the specified lifecycle.
 * Usually used as a replacement of [LiveData] in combination with [BaseViewModel]
 * to avoid LiveData duplication. E.g. you can write this:
 *
 * ```
 * val userLiveValue = liveValue<User>()
 * ```
 *
 * instead of this:
 *
 * ```
 * private val _userLiveData = MutableLiveData<User>()
 * val userLiveData: LiveData<User> = _userLiveData
 * ```
 *
 */
interface LiveValue<T> {

    fun observe(lifecycleOwner: LifecycleOwner, listener: (T) -> Unit)

    fun requireValue(): T

    fun getValue(): T?

}