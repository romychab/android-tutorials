package ua.cn.stu.mvvmsavedstate

class Squares private constructor(
    val size: Int,
    val colors: List<Int>
) {

    constructor(
        size: Int,
        colorProducer: () -> Int
    ) : this(
        size = size,
        colors = (0 until size*size).map { colorProducer() }
    )
}