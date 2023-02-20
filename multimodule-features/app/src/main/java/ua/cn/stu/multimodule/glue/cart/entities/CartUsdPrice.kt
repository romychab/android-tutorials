package ua.cn.stu.multimodule.glue.cart.entities

import ua.cn.stu.multimodule.cart.domain.entities.Price
import ua.cn.stu.multimodule.formatters.PriceFormatter

class CartUsdPrice(
    val usdCents: Int,
    private val formatter: PriceFormatter,
) : Price {

    override val text: String
        get() = formatter.formatPrice(usdCents)

    override fun plus(price: Price): Price {
        return CartUsdPrice(usdCents + (price as CartUsdPrice).usdCents, formatter)
    }

    override fun minus(price: Price): Price {
        return CartUsdPrice(usdCents - (price as CartUsdPrice).usdCents, formatter)
    }

    override fun times(count: Int): Price {
        return CartUsdPrice(usdCents * count, formatter)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CartUsdPrice
        if (usdCents != other.usdCents) return false
        return true
    }

    override fun hashCode(): Int {
        return usdCents
    }


}