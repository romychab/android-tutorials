package ua.cn.stu.multimodule.orders.presentation

interface OrdersRouter {

    /**
     * Close all screens and show initial tabs screen with pre-opened orders.
     */
    fun launchOrdersTab()

}