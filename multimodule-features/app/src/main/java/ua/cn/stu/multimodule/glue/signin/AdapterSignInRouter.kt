package ua.cn.stu.multimodule.glue.signin

import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.navigation.GlobalNavComponentRouter
import ua.cn.stu.multimodule.signin.presentation.SignInRouter
import ua.cn.stu.multimodule.signup.presentation.signup.SignUpFragment
import javax.inject.Inject

class AdapterSignInRouter @Inject constructor(
    private val globalNavComponentRouter: GlobalNavComponentRouter
) : SignInRouter {

    override fun launchSignUp(email: String) {
        val screen = SignUpFragment.Screen(email)
        globalNavComponentRouter.launch(R.id.signUpFragment, screen)
    }

    override fun launchMain() {
        globalNavComponentRouter.startTabs()
    }

}