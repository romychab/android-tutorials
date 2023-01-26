package ua.cn.stu.multimodule.uialerts

import android.content.Context

@Suppress("unused")
object EntryPoint {

    @JvmStatic
    fun get(context: Context): UiAlerts {
        return DefaultUiAlerts(context)
    }

}