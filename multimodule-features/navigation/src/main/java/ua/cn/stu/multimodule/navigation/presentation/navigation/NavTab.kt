package ua.cn.stu.multimodule.navigation.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes

class NavTab(

    /**
     * Destination ID which is launched when a user taps on this tab.
     */
    @IdRes val destinationId: Int,

    /**
     * Tab name displayed under the icon.
     */
    val title: String,

    /**
     * Tab icon.
     */
    @DrawableRes val iconRes: Int

) : java.io.Serializable
