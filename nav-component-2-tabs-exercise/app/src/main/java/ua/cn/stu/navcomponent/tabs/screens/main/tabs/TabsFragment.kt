package ua.cn.stu.navcomponent.tabs.screens.main.tabs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.databinding.FragmentTabsBinding

class TabsFragment : Fragment(R.layout.fragment_tabs) {

    private lateinit var binding: FragmentTabsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentTabsBinding.bind(view)

        TODO("Connect Nav Component to the BottomNavigationView here")
    }

}