package ua.cn.stu.foundation.uiactions

/**
 * Common actions that can be performed in the view-model
 */
interface UiActions {

    /**
     * Display a simple toast message.
     */
    fun toast(message: String)

    /**
     * Get string resource content by its identifier.
     */
    fun getString(messageRes: Int, vararg args: Any): String

}