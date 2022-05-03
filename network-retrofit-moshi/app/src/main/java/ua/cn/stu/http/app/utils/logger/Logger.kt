package ua.cn.stu.http.app.utils.logger

interface Logger {

    fun log(tag: String, message: String)

    fun error(tag: String, e: Throwable)

}