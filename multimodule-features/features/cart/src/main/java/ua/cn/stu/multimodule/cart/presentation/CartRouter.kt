package ua.cn.stu.multimodule.cart.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.multimodule.cart.presentation.quantity.EditQuantityResult

interface CartRouter {

    /**
     * Launch product details screen.
     */
    fun launchProductDetails(productId: Long)

    /**
     * Go back to the previous screen.
     */
    fun goBack()

    /**
     * Launch a screen for editing cart item's quantity.
     */
    fun launchEditQuantity(cartItemId: Long, initialQty: Int)

    /**
     * Launch a screen for making a new order.
     */
    fun launchCreateOrder()

    /**
     * Listen for quantity results sent by screen launched by [launchEditQuantity]
     */
    fun receiveQuantity(): Flow<EditQuantityResult>

    /**
     * Send quantity result from the screen launched by [launchEditQuantity]
     */
    fun sendQuantity(editQuantityResult: EditQuantityResult)

    /**
     * Register back button listener. Callback may return true in order to
     * cancel the default back-button logic.
     */
    fun registerBackHandler(scope: CoroutineScope, handler: () -> Boolean)

}