package ua.cn.stu.multimodule.glue.navigation

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.cn.stu.multimodule.R
import ua.cn.stu.multimodule.navigation.DestinationsProvider
import ua.cn.stu.multimodule.navigation.presentation.navigation.NavTab
import javax.inject.Inject

class DefaultDestinationsProvider @Inject constructor(
    @ApplicationContext private val context: Context
) : DestinationsProvider {

    override fun provideStartDestinationId(): Int {
        return R.id.signInFragment
    }

    override fun provideNavigationGraphId(): Int {
        return R.navigation.nav_graph
    }

    override fun provideMainTabs(): List<NavTab> {
        return listOf(
            NavTab(
                destinationId = R.id.catalogFragment,
                title = context.getString(R.string.tab_catalog),
                iconRes = R.drawable.ic_catalog,
            ),
            NavTab(
                destinationId = R.id.ordersListFragment,
                title = context.getString(R.string.tab_orders),
                iconRes = R.drawable.ic_orders,
            ),
            NavTab(
                destinationId = R.id.profileFragment,
                title = context.getString(R.string.tab_profile),
                iconRes = R.drawable.ic_profile,
            )
        )
    }

    override fun provideTabsDestinationId(): Int {
        return R.id.tabsFragment
    }

    override fun provideCartDestinationId(): Int {
        return R.id.cartListFragment
    }
}