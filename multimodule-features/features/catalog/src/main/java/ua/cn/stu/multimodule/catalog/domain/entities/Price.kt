package ua.cn.stu.multimodule.catalog.domain.entities

interface Price : java.io.Serializable {

    val text: String

    operator fun minus(price: Price): Price
    operator fun plus(price: Price): Price
    operator fun times(proportion: Double): Price
    operator fun compareTo(price: Price): Int
    operator fun div(price: Price): Double
}