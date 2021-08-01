package ua.cn.stu.foundation.views

import java.io.Serializable

/**
 * Base class for defining screen arguments.
 * Please note that all fields inside the screen should be serializable.
 */
interface BaseScreen : Serializable {

    companion object {
        const val ARG_SCREEN = "ARG_SCREEN"
    }

}
