package ua.cn.stu.http.app.screens.main.tabs.dashboard

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.cn.stu.http.app.R
import ua.cn.stu.http.app.databinding.FragmentBoxBinding
import ua.cn.stu.http.app.screens.base.BaseFragment
import ua.cn.stu.http.app.utils.observeEvent
import ua.cn.stu.http.app.utils.viewModelCreator
import ua.cn.stu.http.app.views.DashboardItemView

class BoxFragment : BaseFragment(R.layout.fragment_box) {

    override val viewModel by viewModelCreator { BoxViewModel(getBoxId()) }

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

    private fun getBoxId(): Long = args.boxId

    private fun getColorValue(): Int = args.colorValue

    private fun getColorName(): String = args.colorName
}