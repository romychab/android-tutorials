package ua.cn.stu.multimodule.core.impl

import android.content.Context
import ua.cn.stu.multimodule.core.Resources

/**
 * Default implementation of [Resources] which fetches strings from an application context.
 */
class AndroidResources(
    private val applicationContext: Context,
) : Resources {

    override fun getString(id: Int): String {
        return applicationContext.getString(id)
    }

    override fun getString(id: Int, vararg placeholders: Any): String {
        return applicationContext.getString(id, placeholders)
    }

}