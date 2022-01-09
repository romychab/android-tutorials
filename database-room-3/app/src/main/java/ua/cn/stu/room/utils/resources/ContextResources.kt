package ua.cn.stu.room.utils.resources

import android.content.Context

class ContextResources(
    private val context: Context
) : Resources {

    override fun getString(stringRes: Int): String {
        return context.getString(stringRes)
    }

}