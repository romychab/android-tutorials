package ua.cn.stu.multimodule.glue.orders.entities

import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.orders.domain.entities.Price

class OrderUsdPrice(
    val usdCents: Int,
    private val formatter: PriceFormatter,
) : Price {

    override val text: String
        get() = formatter.formatPrice(usdCents)

    override fun plus(price: Price): Price {
        return OrderUsdPrice(
            usdCents = usdCents + (price as OrderUsdPrice).usdCents,
            formatter = formatter
        )
    }
}