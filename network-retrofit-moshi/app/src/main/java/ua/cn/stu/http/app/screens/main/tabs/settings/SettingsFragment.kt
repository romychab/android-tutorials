package ua.cn.stu.http.app.screens.main.tabs.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.databinding.FragmentSettingsBinding
import ua.cn.stu.http.app.screens.base.BaseFragment
import ua.cn.stu.http.app.utils.observeResults

class SettingsFragment : BaseFragment(R.layout.fragment_settings) {

    override val viewModel by viewModels<SettingsViewModel>()

    private lateinit var binding: FragmentSettingsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)

        binding.resultView.setTryAgainAction { viewModel.tryAgain() }
        val adapter = setupList()
        viewModel.boxSettings.observeResults(this, view, binding.resultView) {
            adapter.renderSettings(it)
        }
    }

    private fun setupList(): SettingsAdapter {
        binding.settingsList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = SettingsAdapter(viewModel)
        binding.settingsList.adapter = adapter
        return adapter
    }

}