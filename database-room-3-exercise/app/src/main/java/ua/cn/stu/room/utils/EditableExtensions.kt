package ua.cn.stu.room.utils

import android.text.Editable

fun Editable?.toCharArray(): CharArray {
    if (this == null) return CharArray(0)
    return CharArray(length).also {
        getChars(0, length, it, 0)
    }
}