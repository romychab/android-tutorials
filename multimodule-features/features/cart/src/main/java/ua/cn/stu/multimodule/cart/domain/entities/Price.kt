package ua.cn.stu.multimodule.cart.domain.entities

interface Price {

    val text: String

    operator fun plus(price: Price): Price

    operator fun minus(price: Price): Price

    operator fun times(count: Int): Price

}