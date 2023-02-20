package ua.cn.stu.multimodule.cart.domain.factories

import ua.cn.stu.multimodule.cart.domain.entities.Price

interface PriceFactory {

    /**
     * Create a zero price.
     */
    val zero: Price

}