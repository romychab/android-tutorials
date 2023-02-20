package ua.cn.stu.multimodule.cart.domain.entities

data class CartItem(
    val id: Long,
    val product: Product,
    val quantity: Int,
    val pricePerItem: Price,
    val discountPerItem: Price,
) {

    val totalPrice: Price get() = pricePerItem * quantity
    val totalPriceWithDiscount: Price get() = (pricePerItem - discountPerItem) * quantity

}