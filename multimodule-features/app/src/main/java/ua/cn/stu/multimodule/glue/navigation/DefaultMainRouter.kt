package ua.cn.stu.multimodule.glue.navigation

import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import ua.cn.stu.multimodule.navigation.presentation.MainRouter
import javax.inject.Inject

class DefaultMainRouter @Inject constructor(
    private val navComponentRouter: GlobalNavComponentRouter
) : MainRouter {

    override fun launchCart() {
        navComponentRouter.launch(R.id.cartListFragment)
    }

}