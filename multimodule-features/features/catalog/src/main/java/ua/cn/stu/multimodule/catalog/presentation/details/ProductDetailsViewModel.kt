package ua.cn.stu.multimodule.catalog.presentation.details

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.catalog.R
import ua.cn.stu.multimodule.catalog.domain.AddToCartUseCase
import ua.cn.stu.multimodule.catalog.domain.GetProductDetailsUseCase
import ua.cn.stu.multimodule.catalog.domain.entities.ProductWithCartInfo
import ua.cn.stu.multimodule.catalog.presentation.details.ProductDetailsFragment.Screen
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.presentation.BaseViewModel

class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted private val screen: Screen,
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
): BaseViewModel() {

    private val addToCartInProgressFlow = MutableStateFlow(false)
    private val productFlow = getProductDetailsUseCase.getProduct(screen.productId)
    val stateLiveValue = combine(productFlow, addToCartInProgressFlow, ::merge)
        .toLiveValue(Container.Pending)

    fun reload() = debounce {
        getProductDetailsUseCase.reload()
    }

    fun addToCart() = debounce {
        viewModelScope.launch {
            val state = stateLiveValue.value.getOrNull() ?: return@launch
            try {
                addToCartInProgressFlow.value = true
                addToCartUseCase.addToCart(state.product)
            } finally {
                addToCartInProgressFlow.value = false
            }
        }
    }

    private fun merge(
        productContainer: Container<ProductWithCartInfo>,
        isAddToCartInProgress: Boolean
    ): Container<State> {
        return productContainer.map { productWithCartInfo ->
            State(
                productWithCartInfo = productWithCartInfo,
                addToCartInProgress = isAddToCartInProgress
            )
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(screen: Screen): ProductDetailsViewModel
    }

    class State(
        private val productWithCartInfo: ProductWithCartInfo,
        private val addToCartInProgress: Boolean,
    ) {
        val product = productWithCartInfo.product
        val showAddToCartProgress: Boolean get() = addToCartInProgress
        val showAddToCartButton: Boolean get() = !addToCartInProgress
        val enableAddToCartButton: Boolean get() = !productWithCartInfo.isInCart
        val addToCartTextRes: Int get() = if (productWithCartInfo.isInCart) {
            R.string.catalog_in_cart
        } else {
            R.string.catalog_add_to_cart
        }
    }
}