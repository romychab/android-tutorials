package ua.cn.stu.multimodule.orders.presentation.orders

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.entities.OnChange
import ua.cn.stu.multimodule.core.presentation.BaseViewModel
import ua.cn.stu.multimodule.orders.domain.CancelOrderUseCase
import ua.cn.stu.multimodule.orders.domain.GetOrdersUseCase
import ua.cn.stu.multimodule.orders.domain.entities.Order
import ua.cn.stu.multimodule.orders.presentation.orders.entities.UiOrder
import javax.inject.Inject

@HiltViewModel
class OrdersListViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
) : BaseViewModel() {

    private val cancellationUuidsFlow = MutableStateFlow(OnChange(mutableSetOf<String>()))

    val stateLiveValue = combine(
        getOrdersUseCase.getOrders(),
        cancellationUuidsFlow,
        ::merge
    ).toLiveValue()

    fun reload() = debounce {
        getOrdersUseCase.reload()
    }

    fun cancelOrder(order: UiOrder) = debounce {
        viewModelScope.launch {
            val cancellationIds = cancellationUuidsFlow.value.value
            cancellationIds.add(order.uuid)
            cancellationUuidsFlow.value = OnChange(cancellationIds)
            try {
                cancelOrderUseCase.cancelOrder(order.origin)
                cancellationIds.remove(order.uuid) // change cancellation state but do not update
            } catch (e: Exception) {
                cancellationIds.remove(order.uuid) // change cancellation state...
                cancellationUuidsFlow.value = OnChange(cancellationIds) // ... and update
                throw e
            }
        }
    }

    private fun merge(
        ordersContainer: Container<List<Order>>,
        cancellations: OnChange<MutableSet<String>>
    ): Container<State> {
        return ordersContainer.map { list ->
            State(
                orders = list.map {
                    UiOrder(
                        origin = it,
                        canCancel = cancelOrderUseCase.canCancel(it),
                        cancelInProgress = cancellations.value.contains(it.uuid)
                    )
                }
            )
        }
    }

    class State(
        val orders: List<UiOrder>
    )

}