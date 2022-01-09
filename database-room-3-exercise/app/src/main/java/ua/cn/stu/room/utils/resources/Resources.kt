package ua.cn.stu.room.utils.resources

import androidx.annotation.StringRes

interface Resources {
    fun getString(@StringRes stringRes: Int): String
}