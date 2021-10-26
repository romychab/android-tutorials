package ua.cn.stu.navcomponent.tabs.screens.main.tabs.dashboard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import ua.cn.stu.navcomponent.tabs.R
import ua.cn.stu.navcomponent.tabs.Repositories
import ua.cn.stu.navcomponent.tabs.databinding.FragmentBoxBinding
import ua.cn.stu.navcomponent.tabs.utils.observeEvent
import ua.cn.stu.navcomponent.tabs.utils.viewModelCreator
import ua.cn.stu.navcomponent.tabs.views.DashboardItemView

class BoxFragment : Fragment(R.layout.fragment_box) {

    private lateinit var binding: FragmentBoxBinding

    private val viewModel by viewModelCreator { BoxViewModel(getBoxId(), Repositories.boxesRepository) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentBoxBinding.bind(view)

        binding.root.setBackgroundColor(DashboardItemView.getBackgroundColor(getColorValue()))
        binding.boxTextView.text = getString(R.string.this_is_box, getColorName())

        binding.goBackButton.setOnClickListener { onGoBackButtonPressed() }

        listenShouldExitEvent()
    }

    private fun onGoBackButtonPressed() {
        TODO("Go back to the previous screen here")
    }

    private fun listenShouldExitEvent() = viewModel.shouldExitEvent.observeEvent(viewLifecycleOwner) { shouldExit ->
        if (shouldExit) {
            // close the screen if the box has been deactivated
            TODO("Go back to the previous screen here")
        }
    }

    private fun getBoxId(): Int {
        TODO("Extract box id from arguments here")
    }

    private fun getColorValue(): Int {
        TODO("Extract color value from arguments here")
    }

    private fun getColorName(): String {
        TODO("Extract color name from arguments here")
    }

}