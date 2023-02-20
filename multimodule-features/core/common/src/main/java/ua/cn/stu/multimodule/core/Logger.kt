package ua.cn.stu.multimodule.core

/**
 * Global in-app logger.
 */
interface Logger {

    fun log(message: String)

    fun err(exception: Throwable, message: String? = null)

}