package ua.cn.stu.multimodule.cart.presentation.cartlist

import android.graphics.Color
import android.widget.ImageView
import androidx.core.view.isVisible
import com.elveum.elementadapter.setTintColor
import com.elveum.elementadapter.simpleAdapter
import ua.cn.stu.multimodule.cart.databinding.ItemCartProductBinding
import ua.cn.stu.multimodule.cart.presentation.cartlist.entities.UiCartItem
import ua.cn.stu.multimodule.core.presentation.loadUrl

interface CartAdapterListener {
    fun onIncrement(cartItem: UiCartItem)
    fun onDecrement(cartItem: UiCartItem)
    fun onChangeQuantity(cartItem: UiCartItem)
    fun onChosen(cartItem: UiCartItem)
    fun onToggle(cartItem: UiCartItem)
}

fun createCartAdapter(
    listener: CartAdapterListener
) = simpleAdapter<UiCartItem, ItemCartProductBinding> {

    areItemsSame = { oldItem, newItem -> oldItem.id == newItem.id }
    areContentsSame = { oldItem, newItem -> oldItem == newItem }

    bind { cartItem ->
        nameTextView.text = cartItem.name
        productImageView.loadUrl(cartItem.imageUrl)
        quantityTextView.text = cartItem.quantity.toString()
        decrementImageView.setEnabledAndTint(cartItem.canDecrement)
        incrementImageView.setEnabledAndTint(cartItem.canIncrement)
        originPriceTextView.text = cartItem.totalOriginPrice.text
        priceWithDiscountTextView.text = cartItem.totalDiscountPrice.text
        checkBox.isVisible = cartItem.showCheckBox
        checkBox.isChecked = cartItem.isChecked
        if (cartItem.totalOriginPrice == cartItem.totalDiscountPrice) {
            priceWithDiscountTextView.isVisible = false
            originPriceTextView.setBackgroundColor(Color.TRANSPARENT)
        } else {
            priceWithDiscountTextView.isVisible = true
            originPriceTextView.setBackgroundResource(ua.cn.stu.multimodule.theme.R.drawable.core_theme_diagonal_line)
        }
    }

    listeners {
        incrementImageView.onClick { listener.onIncrement(it) }
        decrementImageView.onClick { listener.onDecrement(it) }
        quantityTextView.onClick { listener.onChangeQuantity(it) }
        root.onClick { listener.onChosen(it) }
        root.onLongClick {
            listener.onToggle(it)
            true
        }
        checkBox.onClick { listener.onToggle(it) }
    }
}

private fun ImageView.setEnabledAndTint(isEnabled: Boolean) {
    this.isEnabled = isEnabled
    if (isEnabled) {
        this.setTintColor(ua.cn.stu.multimodule.theme.R.color.action)
    } else {
        this.setTintColor(ua.cn.stu.multimodule.theme.R.color.action_disabled)
    }
}