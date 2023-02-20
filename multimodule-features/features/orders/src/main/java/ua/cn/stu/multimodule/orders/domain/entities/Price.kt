package ua.cn.stu.multimodule.orders.domain.entities

interface Price {
    val text: String

    operator fun plus(price: Price): Price

}