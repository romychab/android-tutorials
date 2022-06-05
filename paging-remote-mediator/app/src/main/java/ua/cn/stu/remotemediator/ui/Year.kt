package ua.cn.stu.remotemediator.ui

import android.content.Context
import ua.cn.stu.remotemediator.R

class Year(
    val value: Int?,
    private val message: String
) {

    constructor(context: Context, year: Int?) : this(
        value = year,
        message = year?.toString() ?: context.getString(R.string.all)
    )

    override fun toString(): String {
        return message
    }
}
