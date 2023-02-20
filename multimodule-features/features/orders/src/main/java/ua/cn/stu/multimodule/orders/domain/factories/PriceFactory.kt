package ua.cn.stu.multimodule.orders.domain.factories

import ua.cn.stu.multimodule.orders.domain.entities.Price

interface PriceFactory {

    /**
     * Create a zero price.
     */
    val zero: Price

}