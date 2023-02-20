package ua.cn.stu.multimodule.formatters

interface DateTimeFormatter {

    /**
     * Covert timestamp in milliseconds to a user-friendly date-time string.
     */
    fun formatDateTime(millis: Long): String

}