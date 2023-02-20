package ua.cn.stu.multimodule.glue.catalog

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.catalog.domain.entities.ProductFilter
import ua.cn.stu.multimodule.catalog.presentation.CatalogFilterRouter
import ua.cn.stu.multimodule.catalog.presentation.filter.CatalogFilterFragment
import ua.cn.stu.multimodule.core.ScreenCommunication
import ua.cn.stu.multimodule.core.listen
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterCatalogFilterRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
    private val screenCommunication: ScreenCommunication,
) : CatalogFilterRouter {

    override fun launchFilter(initialFilter: ProductFilter) {
        globalNavComponentRouter.launch(
            R.id.catalogFilterFragment,
            CatalogFilterFragment.Screen(initialFilter)
        )
    }

    override fun receiveFilterResults(): Flow<ProductFilter> {
        return screenCommunication.listen(ProductFilter::class.java)
    }

    override fun sendFilterResults(filter: ProductFilter) {
        screenCommunication.publishResult(filter)
    }

    override fun goBack() {
        globalNavComponentRouter.pop()
    }

    override fun registerBackHandler(scope: CoroutineScope, handler: () -> Boolean) {
        globalNavComponentRouter.registerBackHandler(scope, handler)
    }

}