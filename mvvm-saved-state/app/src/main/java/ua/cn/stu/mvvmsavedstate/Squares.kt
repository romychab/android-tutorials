package ua.cn.stu.mvvmsavedstate

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Squares private constructor(
    val size: Int,
    val colors: List<Int>
) : Parcelable {

    constructor(
        size: Int,
        colorProducer: () -> Int
    ) : this(
        size = size,
        colors = (0 until size*size).map { colorProducer() }
    )
}