package ua.cn.stu.multimodule.cart.presentation.cartlist.entities

import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.entities.Price
import ua.cn.stu.multimodule.cart.domain.entities.Product

data class UiCartItem(
    val origin: CartItem,
    val isChecked: Boolean,
    val showCheckBox: Boolean,
    val minQuantity: Int,
    val maxQuantity: Int,
) {

    val id: Long get() = origin.id
    val product: Product get() = origin.product
    val imageUrl: String get() = product.photo
    val quantity: Int get() = origin.quantity
    val name: String get() = product.name
    val totalOriginPrice: Price get() = origin.totalPrice
    val totalDiscountPrice: Price get() = origin.totalPriceWithDiscount
    val canIncrement: Boolean get() = quantity < maxQuantity
    val canDecrement: Boolean get() = quantity > minQuantity

}