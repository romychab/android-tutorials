package ua.cn.stu.multimodule.navigation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import ua.cn.stu.multimodule.navigation.presentation.TabsFragment
import ua.cn.stu.multimodule.navigation.presentation.navigation.NavTab

interface DestinationsProvider {

    /**
     * Get the destination ID of the initial start screen.
     */
    @IdRes
    fun provideStartDestinationId(): Int

    /**
     * Get the ID of a main navigation graph.
     */
    @NavigationRes
    fun provideNavigationGraphId(): Int

    /**
     * Get the list of tabs to be rendered at the bottom nav bar.
     */
    fun provideMainTabs(): List<NavTab>

    /**
     * Get the destination ID of [TabsFragment]
     */
    @IdRes
    fun provideTabsDestinationId(): Int

    /**
     * Get the destination ID of fragment which shows a cart to the user.
     */
    @IdRes
    fun provideCartDestinationId(): Int

}