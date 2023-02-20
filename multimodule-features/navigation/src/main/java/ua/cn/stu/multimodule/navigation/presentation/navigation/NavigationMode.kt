package ua.cn.stu.multimodule.navigation.presentation.navigation


sealed class NavigationMode : java.io.Serializable {

    /**
     * Simple stack navigation
     */
    object Stack : NavigationMode()

    /**
     * Simple stack navigation but the initial screen contains bottom tabs
     * defined in [tabs] argument.
     */
    class Tabs(
        val tabs: ArrayList<NavTab>,
        val startTabDestinationId: Int?,
    ) : NavigationMode()

}