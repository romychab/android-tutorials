package ua.cn.stu.foundation.sideeffects.navigator.plugin

import android.content.Context
import ua.cn.stu.foundation.sideeffects.SideEffectPlugin
import ua.cn.stu.foundation.sideeffects.SideEffectMediator
import ua.cn.stu.foundation.sideeffects.navigator.Navigator

/**
 * This plugin allows using [Navigator] interface in view-models.
 * This plugin may support different navigator implementations so you should pass the
 * desired navigator to the constructor. Now there is one default implementation: [StackFragmentNavigator].
 * Allows adding [Navigator] interface to view-model constructor.
 */
class NavigatorPlugin(
    private val navigator: Navigator,
) : SideEffectPlugin<Navigator, Navigator> {

    override val mediatorClass: Class<Navigator>
        get() = Navigator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Navigator> {
        return NavigatorSideEffectMediator()
    }

    override fun createImplementation(mediator: Navigator): Navigator {
        return navigator
    }

}