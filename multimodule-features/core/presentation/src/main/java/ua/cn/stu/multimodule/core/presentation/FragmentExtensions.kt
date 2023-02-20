@file:Suppress("DEPRECATION", "UNCHECKED_CAST")

package ua.cn.stu.multimodule.core.presentation

import androidx.fragment.app.Fragment

/**
 * Default arg name for holding screen args in fragments. Use this name
 * if you want to integrate your navigation with the core.
 */
const val ARG_SCREEN = "screen"

/**
 * Get screen args attached to the [Fragment].
 */
fun <T : BaseScreen> Fragment.args(): T {
    return requireArguments().getSerializable(ARG_SCREEN) as? T
        ?: throw IllegalStateException("Screen args don't exist")
}
