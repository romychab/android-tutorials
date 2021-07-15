package ua.cn.stu.mvvmnavigation.navigator

import androidx.annotation.StringRes
import ua.cn.stu.mvvmnavigation.screens.base.BaseScreen

interface Navigator {

    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)

    fun toast(@StringRes messageRes: Int)

    fun getString(@StringRes messageRes: Int): String

}