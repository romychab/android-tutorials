package ua.cn.stu.foundation.sideeffects.resources.plugin

import android.content.Context
import ua.cn.stu.foundation.sideeffects.SideEffectMediator
import ua.cn.stu.foundation.sideeffects.resources.Resources

class ResourcesSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return appContext.getString(resId, *args)
    }

}