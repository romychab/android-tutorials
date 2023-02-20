package ua.cn.stu.multimodule.core.presentation.live

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 * One-time side-effect event to be used in [LiveValue] or [LiveData].
 */
class Event<T>(
    value: T
) {

    private var _value: T? = value

    fun get(): T? = _value.also { _value = null }

}

/**
 * Short name of live value with event.
 */
typealias LiveEventValue<T> = LiveValue<Event<T>>

/**
 * Extension method for simpler event observing.
 */
fun <T> LiveValue<Event<T>>.observeEvent(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) {
    observe(lifecycleOwner) { event ->
        val value = event.get() ?: return@observe
        observer(value)
    }
}