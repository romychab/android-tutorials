package ua.cn.stu.multimodule.formatters

interface PriceFormatter {

    /**
     * Convert cents into user-friendly price text.
     */
    fun formatPrice(usdCents: Int): String

}