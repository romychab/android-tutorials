package ua.cn.stu.multimodule.cart.domain.entities

import ua.cn.stu.multimodule.cart.domain.factories.PriceFactory

data class Cart(
    val cartItems: List<CartItem>,
    private val priceFactory: PriceFactory,
) {
    val totalPrice: Price get() {
        if (cartItems.isEmpty()) return priceFactory.zero
        return cartItems
            .map { it.totalPrice }
            .reduce { acc, price -> acc + price }
    }

    val totalPriceWithDiscount: Price get() {
        if (cartItems.isEmpty()) return priceFactory.zero
        return cartItems
            .map { it.totalPriceWithDiscount }
            .reduce { acc, price -> acc + price }
    }

}