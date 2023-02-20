package ua.cn.stu.multimodule.glue.catalog

import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.catalog.presentation.CatalogRouter
import ua.cn.stu.multimodule.catalog.presentation.details.ProductDetailsFragment
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterCatalogRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
) : CatalogRouter {

    override fun launchDetails(productId: Long) {
        globalNavComponentRouter.launch(
            R.id.productDetailsFragment,
            ProductDetailsFragment.Screen(productId)
        )
    }

    override fun launchCreateOrder() {
        globalNavComponentRouter.launch(R.id.createOrderFragment)
    }

}