package ua.cn.stu.simplemvvm

import android.os.Bundle
import ua.cn.stu.foundation.sideeffects.navigator.plugin.StackFragmentNavigator
import ua.cn.stu.foundation.sideeffects.navigator.plugin.NavigatorPlugin
import ua.cn.stu.foundation.sideeffects.SideEffectPluginsManager
import ua.cn.stu.foundation.sideeffects.dialogs.plugin.DialogsPlugin
import ua.cn.stu.foundation.sideeffects.intents.plugin.IntentsPlugin
import ua.cn.stu.foundation.sideeffects.permissions.plugin.PermissionsPlugin
import ua.cn.stu.foundation.sideeffects.resources.plugin.ResourcesPlugin
import ua.cn.stu.foundation.sideeffects.toasts.plugin.ToastsPlugin
import ua.cn.stu.foundation.views.activity.BaseActivity
import ua.cn.stu.simplemvvm.views.currentcolor.CurrentColorFragment

/**
 * This application is a single-activity app. MainActivity is a container
 * for all screens.
 */
class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun createNavigator() = StackFragmentNavigator(
            containerId = R.id.fragmentContainer,
            defaultTitle = getString(R.string.app_name),
            animations = StackFragmentNavigator.Animations(
                enterAnim = R.anim.enter,
                exitAnim = R.anim.exit,
                popEnterAnim = R.anim.pop_enter,
                popExitAnim = R.anim.pop_exit
            ),
            initialScreenCreator = { CurrentColorFragment.Screen() }
        )

}