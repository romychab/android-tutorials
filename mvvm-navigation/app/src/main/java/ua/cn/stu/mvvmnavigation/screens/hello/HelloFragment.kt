package ua.cn.stu.mvvmnavigation.screens.hello

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.cn.stu.mvvmnavigation.databinding.FragmentHelloBinding
import ua.cn.stu.mvvmnavigation.screens.base.BaseFragment
import ua.cn.stu.mvvmnavigation.screens.base.BaseScreen
import ua.cn.stu.mvvmnavigation.screens.base.screenViewModel

class HelloFragment : BaseFragment() {

    class Screen : BaseScreen

    override val viewModel by screenViewModel<HelloViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentHelloBinding.inflate(inflater, container, false)

        binding.editButton.setOnClickListener { viewModel.onEditPressed() }

        viewModel.currentMessageLiveData.observe(viewLifecycleOwner) {
            binding.valueTextView.text = it
        }

        return binding.root
    }

}