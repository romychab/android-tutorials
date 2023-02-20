package ua.cn.stu.multimodule.navigation.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.presentation.BaseViewModel
import ua.cn.stu.multimodule.navigation.domain.GetCartItemsCountUseCase
import ua.cn.stu.multimodule.navigation.domain.GetCurrentUsernameUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentUsernameUseCase: GetCurrentUsernameUseCase,
    private val getCartItemsCountUseCase: GetCartItemsCountUseCase,
    private val router: MainRouter,
) : BaseViewModel() {

    val usernameLiveValue = liveValue<String?>()
    val cartLiveValue = liveValue<CartState>()

    init {
        observeUsername()
        observeCart()
    }

    fun launchCart() = debounce {
        router.launchCart()
    }

    private fun observeUsername() {
        viewModelScope.launch {
            getCurrentUsernameUseCase.getUsername().collectLatest { container ->
                if (container is Container.Success) {
                    usernameLiveValue.value = container.value
                } else {
                    usernameLiveValue.value = null
                }
            }
        }
    }

    private fun observeCart() {
        viewModelScope.launch {
            getCartItemsCountUseCase.getCartItemCount().collectLatest { container ->
                if (container is Container.Success) {
                    val maxCount = 9
                    val countString = if (container.value > maxCount) {
                        "$maxCount+"
                    } else if (container.value == 0) {
                        ""
                    } else {
                        container.value.toString()
                    }
                    cartLiveValue.value = CartState(countString)
                } else {
                    cartLiveValue.value = CartState(itemsCountDisplayString = "")
                }
            }
        }
    }

    class CartState(
        val itemsCountDisplayString: String,
    ) {
        val showCartIcon: Boolean get() = itemsCountDisplayString.isNotBlank()
    }

}