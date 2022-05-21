package ua.cn.stu.hilt.app.screens.main.tabs.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.cn.stu.hilt.app.R
import ua.cn.stu.hilt.app.databinding.FragmentBoxBinding
import ua.cn.stu.hilt.app.screens.base.BaseFragment
import ua.cn.stu.hilt.app.utils.observeEvent
import ua.cn.stu.hilt.app.utils.viewModelCreator
import ua.cn.stu.hilt.app.views.DashboardItemView
import javax.inject.Inject

@AndroidEntryPoint
class BoxFragment : BaseFragment(R.layout.fragment_box) {

    @Inject lateinit var factory: BoxViewModel.Factory
    override val viewModel by viewModelCreator {
        factory.create(args.boxId)
    }

    private lateinit var binding: FragmentBoxBinding

    private val args by navArgs<BoxFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(view)

        binding.root.setBackgroundColor(DashboardItemView.getBackgroundColor(getColorValue()))
        binding.boxTextView.text = getString(R.string.this_is_box, getColorName())

        binding.goBackButton.setOnClickListener { onGoBackButtonPressed() }

        listenShouldExitEvent()
    }

    private fun onGoBackButtonPressed() {
        findNavController().popBackStack()
    }

    private fun listenShouldExitEvent() = viewModel.shouldExitEvent.observeEvent(viewLifecycleOwner) { shouldExit ->
        if (shouldExit) {
            // close the screen if the box has been deactivated
            findNavController().popBackStack()
        }
    }

    private fun getColorValue(): Int = args.colorValue

    private fun getColorName(): String = args.colorName
}