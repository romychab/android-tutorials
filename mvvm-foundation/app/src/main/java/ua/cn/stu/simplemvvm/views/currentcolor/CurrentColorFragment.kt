package ua.cn.stu.simplemvvm.views.currentcolor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.cn.stu.simplemvvm.databinding.FragmentCurrentColorBinding
import ua.cn.stu.simplemvvm.views.base.BaseFragment
import ua.cn.stu.simplemvvm.views.base.BaseScreen
import ua.cn.stu.simplemvvm.views.base.screenViewModel

class CurrentColorFragment : BaseFragment() {

    // no arguments for this screen
    class Screen : BaseScreen

    override val viewModel by screenViewModel<CurrentColorViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentCurrentColorBinding.inflate(inflater, container, false)

        viewModel.currentColor.observe(viewLifecycleOwner) {
            binding.colorView.setBackgroundColor(it.value)
        }

        binding.changeColorButton.setOnClickListener {
            viewModel.changeColor()
        }

        return binding.root
    }


}