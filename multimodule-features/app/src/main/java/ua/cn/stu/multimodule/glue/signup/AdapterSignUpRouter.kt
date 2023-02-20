package ua.cn.stu.multimodule.glue.signup

import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import ua.cn.stu.multimodule.signup.presentation.SignUpRouter
import javax.inject.Inject

class AdapterSignUpRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SignUpRouter {

    override fun goBack() {
        globalNavComponentRouter.pop()
    }

}