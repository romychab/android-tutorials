package ua.cn.stu.multimodule.core

/**
 * Get string resources without direct context dependency.
 */
interface Resources {

    fun getString(id: Int): String

    fun getString(id: Int, vararg placeholders: Any): String

}