package ua.cn.stu.multimodule.glue.orders.factories

import ua.cn.stu.multimodule.formatters.PriceFormatter
import ua.cn.stu.multimodule.glue.orders.entities.OrderUsdPrice
import ua.cn.stu.multimodule.orders.domain.entities.Price
import ua.cn.stu.multimodule.orders.domain.factories.PriceFactory
import javax.inject.Inject

class DefaultOrderPriceFactory @Inject constructor(
    private val priceFormatter: PriceFormatter,
) : PriceFactory {

    override val zero: Price
        get() = OrderUsdPrice(usdCents = 0, formatter = priceFormatter)
}