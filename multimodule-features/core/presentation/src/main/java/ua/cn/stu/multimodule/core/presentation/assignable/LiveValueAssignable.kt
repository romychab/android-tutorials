package ua.cn.stu.multimodule.core.presentation.assignable

import ua.cn.stu.multimodule.core.presentation.live.LiveValue
import ua.cn.stu.multimodule.core.presentation.live.MutableLiveValue

internal class LiveValueAssignable<T>(
    private val liveValue: LiveValue<T>
) : Assignable<T> {

    override fun setValue(value: T) {
        (liveValue as? MutableLiveValue<T>)?.setValue(value)
    }
}