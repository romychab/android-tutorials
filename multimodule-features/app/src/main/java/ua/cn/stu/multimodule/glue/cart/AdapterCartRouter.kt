package ua.cn.stu.multimodule.glue.cart

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.cart.presentation.CartRouter
import ua.cn.stu.multimodule.cart.presentation.quantity.EditQuantityDialogFragment
import ua.cn.stu.multimodule.cart.presentation.quantity.EditQuantityResult
import ua.cn.stu.multimodule.catalog.presentation.details.ProductDetailsFragment
import ua.cn.stu.multimodule.core.ScreenCommunication
import ua.cn.stu.multimodule.core.listen
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import javax.inject.Inject

class AdapterCartRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter,
    private val screenCommunication: ScreenCommunication,
) : CartRouter {

    override fun launchProductDetails(productId: Long) {
        globalNavComponentRouter.launch(
            R.id.productDetailsFragment,
            ProductDetailsFragment.Screen(productId)
        )
    }

    override fun goBack() {
        globalNavComponentRouter.pop()
    }

    override fun launchEditQuantity(cartItemId: Long, initialQty: Int) {
        globalNavComponentRouter.launch(
            R.id.editQuantityDialogFragment,
            EditQuantityDialogFragment.Screen(cartItemId, initialQty)
        )
    }

    override fun launchCreateOrder() {
        globalNavComponentRouter.launch(R.id.createOrderFragment)
    }

    override fun receiveQuantity(): Flow<EditQuantityResult> {
        return screenCommunication.listen(EditQuantityResult::class.java)
    }

    override fun sendQuantity(editQuantityResult: EditQuantityResult) {
        screenCommunication.publishResult(editQuantityResult)
    }

    override fun registerBackHandler(scope: CoroutineScope, handler: () -> Boolean) {
        globalNavComponentRouter.registerBackHandler(scope, handler)
    }

}