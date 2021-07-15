package ua.cn.stu.mvvmnavigation.screens.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ua.cn.stu.mvvmnavigation.databinding.FragmentEditBinding
import ua.cn.stu.mvvmnavigation.screens.base.BaseFragment
import ua.cn.stu.mvvmnavigation.screens.base.BaseScreen
import ua.cn.stu.mvvmnavigation.screens.base.screenViewModel

class EditFragment : BaseFragment() {

    // this screen accepts a string value from the HelloFragment
    class Screen(
        val initialValue: String
    ) : BaseScreen

    override val viewModel by screenViewModel<EditViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = FragmentEditBinding.inflate(inflater, container, false)

        // listening for the initial value to be assigned into the valueEditText
        viewModel.initialMessageEvent.observe(viewLifecycleOwner) {
            it.getValue()?.let { message -> binding.valueEditText.setText(message) }
        }

        // buttons' click listeners
        binding.saveButton.setOnClickListener {
            viewModel.onSavePressed(binding.valueEditText.text.toString())
        }
        binding.cancelButton.setOnClickListener {
            viewModel.onCancelPressed()
        }

        return binding.root
    }
}