package ua.cn.stu.multimodule.profile.presentation

interface ProfileRouter {

    /**
     * Launch the screen for editing username.
     */
    fun launchEditUsername()

    /**
     * Go back to the previous screen.
     */
    fun goBack()

    /**
     * Close all screens and launch the initial screen.
     */
    fun restartApp()

}