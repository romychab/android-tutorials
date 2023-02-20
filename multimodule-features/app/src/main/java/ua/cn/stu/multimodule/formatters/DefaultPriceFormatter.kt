package ua.cn.stu.multimodule.formatters

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultPriceFormatter @Inject constructor() : PriceFormatter{

    override fun formatPrice(usdCents: Int): String {
        return "\$${String.format("%.2f", usdCents / 100f)}"
    }

}