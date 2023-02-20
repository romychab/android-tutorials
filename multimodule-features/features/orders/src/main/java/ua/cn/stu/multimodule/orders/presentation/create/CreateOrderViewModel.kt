package ua.cn.stu.multimodule.orders.presentation.create

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.core.AlertDialogConfig
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.presentation.BaseViewModel
import ua.cn.stu.multimodule.orders.R
import ua.cn.stu.multimodule.orders.domain.CreateOrderUseCase
import ua.cn.stu.multimodule.orders.domain.GetCartUseCase
import ua.cn.stu.multimodule.orders.domain.entities.Cart
import ua.cn.stu.multimodule.orders.domain.entities.Field
import ua.cn.stu.multimodule.orders.domain.entities.Recipient
import ua.cn.stu.multimodule.orders.domain.exceptions.EmptyFieldException
import ua.cn.stu.multimodule.orders.presentation.OrdersRouter
import javax.inject.Inject

@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    private val getCartUseCase: GetCartUseCase,
    private val createOrderUseCase: CreateOrderUseCase,
    private val router: OrdersRouter,
) : BaseViewModel() {

    private val createOrderInProgressFlow = MutableStateFlow(false)
    private val cartFlow = MutableStateFlow<Container<Cart>>(Container.Pending)

    val stateLiveValue = combine(cartFlow, createOrderInProgressFlow, ::merge)
        .toLiveValue(Container.Pending)

    val emptyFieldErrorLiveEvent = liveEvent<Field>()

    init {
        load()
    }

    fun load() = debounce {
        loadScreenInto(cartFlow) {
            getCartUseCase.getCart()
        }
    }

    fun createOrder(recipient: Recipient) = debounce {
        viewModelScope.launch {
            val cart = (stateLiveValue.value as? Container.Success)?.value?.cart ?: return@launch
            try {
                createOrderInProgressFlow.value = true
                createOrderUseCase.createOrder(cart, recipient)
                createOrderInProgressFlow.value = false
                showSuccessDialog()
                router.launchOrdersTab()
            } catch (e: EmptyFieldException) {
                emptyFieldErrorLiveEvent.publish(e.field)
            } finally {
                createOrderInProgressFlow.value = false
            }
        }
    }

    private suspend fun showSuccessDialog() {
        commonUi.alertDialog(
            AlertDialogConfig(
                title = resources.getString(R.string.orders_congrats),
                message = resources.getString(R.string.orders_order_created),
                positiveButton = resources.getString(R.string.orders_create_order_ok)
            )
        )
    }

    private fun merge(
        cartContainer: Container<Cart>,
        createOrderInProgress: Boolean
    ): Container<State> {
        return cartContainer.map {
            State(it, createOrderInProgress)
        }
    }

    class State(
        val cart: Cart,
        private val createOrderInProgress: Boolean,
    ) {
        val enableCreateOrderButton: Boolean get() = !createOrderInProgress
        val showProgressBar: Boolean get() = createOrderInProgress
    }

}