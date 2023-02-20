package ua.cn.stu.multimodule.catalog.presentation.catalog

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import ua.cn.stu.multimodule.catalog.domain.AddToCartUseCase
import ua.cn.stu.multimodule.catalog.domain.GetCatalogUseCase
import ua.cn.stu.multimodule.catalog.domain.entities.ProductFilter
import ua.cn.stu.multimodule.catalog.domain.entities.ProductWithCartInfo
import ua.cn.stu.multimodule.catalog.presentation.CatalogFilterRouter
import ua.cn.stu.multimodule.catalog.presentation.CatalogRouter
import ua.cn.stu.multimodule.core.Container
import ua.cn.stu.multimodule.core.presentation.BaseViewModel
import javax.inject.Inject

@HiltViewModel
@ExperimentalCoroutinesApi
class CatalogViewModel @Inject constructor(
    private val getCatalogUseCase: GetCatalogUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val catalogRouter: CatalogRouter,
    private val catalogFilterRouter: CatalogFilterRouter,
) : BaseViewModel() {

    private val filterFlow = MutableStateFlow(ProductFilter.EMPTY)

    val stateLiveValue = filterFlow
        .flatMapLatest { filter ->
            getCatalogUseCase.getProducts(filter).map { container ->
                container.map { products ->
                    State(products, filter)
                }
            }
        }
        .toLiveValue(initialValue = Container.Pending)

    init {
        viewModelScope.launch {
            catalogFilterRouter.receiveFilterResults().collectLatest {
                filterFlow.value = it
            }
        }
    }

    fun launchFilter() = debounce {
        catalogFilterRouter.launchFilter(filterFlow.value)
    }

    fun launchDetails(productWithCartInfo: ProductWithCartInfo) = debounce {
        catalogRouter.launchDetails(productWithCartInfo.product.id)
    }

    fun addToCart(productWithCartInfo: ProductWithCartInfo) = debounce {
        viewModelScope.launch {
            addToCartUseCase.addToCart(productWithCartInfo.product)

        }
    }

    class State(
        val products: List<ProductWithCartInfo>,
        val filter: ProductFilter,
    )

}