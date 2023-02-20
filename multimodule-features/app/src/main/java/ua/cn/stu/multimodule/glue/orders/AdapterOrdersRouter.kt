package ua.cn.stu.multimodule.glue.orders

import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import ua.cn.stu.multimodule.orders.presentation.OrdersRouter
import javax.inject.Inject

class AdapterOrdersRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
): OrdersRouter {

    override fun launchOrdersTab() {
        globalNavComponentRouter.startTabs(startTabDestinationId = R.id.ordersListFragment)
    }

}