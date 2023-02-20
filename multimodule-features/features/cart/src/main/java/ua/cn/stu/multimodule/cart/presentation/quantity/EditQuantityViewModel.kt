package ua.cn.stu.multimodule.cart.presentation.quantity

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.cart.R
import ua.cn.stu.multimodule.cart.domain.GetCartUseCase
import ua.cn.stu.multimodule.cart.domain.ValidateCartItemQuantityUseCase
import ua.cn.stu.multimodule.cart.domain.entities.CartItem
import ua.cn.stu.multimodule.cart.domain.exceptions.QuantityOutOfRangeException
import ua.cn.stu.multimodule.cart.presentation.CartRouter
import ua.cn.stu.multimodule.cart.presentation.quantity.EditQuantityDialogFragment.Screen
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.presentation.BaseViewModel

class EditQuantityViewModel @AssistedInject constructor(
    @Assisted private val screen: Screen,
    private val router: CartRouter,
    private val getCartUseCase: GetCartUseCase,
    private val validateCartItemQuantityUseCase: ValidateCartItemQuantityUseCase,
) : BaseViewModel() {

    val initialQuantityLiveEvent = liveEvent<Int>()
    val stateLiveValue = liveValue<Container<State>>()

    init {
        initialQuantityLiveEvent.publish(screen.initialQuantity)
        load()
    }

    fun load() = debounce {
        loadScreenInto(stateLiveValue) {
            val cartItem = getCartUseCase.getCartById(screen.cartItemId)
            val maxQuantity = validateCartItemQuantityUseCase.getMaxQuantity(cartItem)
            State(cartItem, maxQuantity)
        }
    }

    fun saveNewQuantity(input: String) = debounce {
        val cartItem = stateLiveValue.getValue()?.getOrNull()?.cartItem ?: return@debounce
        val parsedQuantity = try {
            input.toInt()
        } catch (e: Exception) {
            commonUi.toast(resources.getString(R.string.cart_invalid_quantity))
            return@debounce
        }
        viewModelScope.launch {
            try {
                validateCartItemQuantityUseCase.validateNewQuantity(cartItem, parsedQuantity)
                router.sendQuantity(EditQuantityResult(cartItem.id, parsedQuantity))
                router.goBack()
            } catch (e: QuantityOutOfRangeException) {
                commonUi.toast(resources.getString(R.string.cart_quantity_out_of_range))
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(screen: Screen): EditQuantityViewModel
    }

    class State(
        val cartItem: CartItem,
        val maxQuantity: Int
    )

}