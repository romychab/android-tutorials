package ua.cn.stu.room.screens.main.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import ua.cn.stu.room.R
import ua.cn.stu.room.Repositories
import ua.cn.stu.room.databinding.FragmentTabsBinding
import ua.cn.stu.room.utils.viewModelCreator

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding

    private val viewModel by viewModelCreator { TabsViewModel(Repositories.accountsRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)

        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        observeAdminTab()
    }

    private fun observeAdminTab() {
        viewModel.showAdminTab.observe(viewLifecycleOwner) { showAdminTab ->
            binding.bottomNavigationView.menu.findItem(R.id.admin).isVisible = showAdminTab
        }
    }

}