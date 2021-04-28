package ua.cn.stu.navigation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import ua.cn.stu.navigation.R
import ua.cn.stu.navigation.contract.CustomAction
import ua.cn.stu.navigation.contract.HasCustomAction
import ua.cn.stu.navigation.contract.HasCustomTitle
import ua.cn.stu.navigation.contract.navigator
import ua.cn.stu.navigation.Options
import ua.cn.stu.navigation.databinding.FragmentOptionsBinding

class OptionsFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var binding: FragmentOptionsBinding

    private lateinit var options: Options

    private lateinit var boxCountItems: List<BoxCountItem>
    private lateinit var adapter: ArrayAdapter<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable<Options>(KEY_OPTIONS) ?:
                arguments?.getParcelable(ARG_OPTIONS) ?:
                throw IllegalArgumentException("You need to specify options to launch this fragment")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentOptionsBinding.inflate(inflater, container, false)

        setupSpinner()
        setupCheckBox()
        updateUi()

        binding.cancelButton.setOnClickListener { onCancelPressed() }
        binding.confirmButton.setOnClickListener { onConfirmPressed() }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    override fun getTitleRes(): Int = R.string.options

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    private fun setupSpinner() {
        boxCountItems = (1..6).map { BoxCountItem(it, resources.getQuantityString(R.plurals.boxes, it, it)) }
        adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner,
            boxCountItems
        )
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1)

        binding.boxCountSpinner.adapter = adapter
        binding.boxCountSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val count = boxCountItems[position].count
                options = options.copy(boxCount = count)
            }
        }
    }

    private fun setupCheckBox() {
        binding.enableTimerCheckBox.setOnClickListener {
            options = options.copy(isTimerEnabled = binding.enableTimerCheckBox.isChecked)
        }
    }

    private fun updateUi() {
        binding.enableTimerCheckBox.isChecked = options.isTimerEnabled

        val currentIndex = boxCountItems.indexOfFirst { it.count == options.boxCount }
        binding.boxCountSpinner.setSelection(currentIndex)
    }

    private fun onCancelPressed() {
        navigator().goBack()
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }

    companion object {
        @JvmStatic private val ARG_OPTIONS = "ARG_OPTIONS"
        @JvmStatic private val KEY_OPTIONS = "KEY_OPTIONS"

        @JvmStatic
        fun newInstance(options: Options): OptionsFragment {
            val args = Bundle()
            args.putParcelable(ARG_OPTIONS, options)
            val fragment = OptionsFragment()
            fragment.arguments = args
            return fragment
        }

    }

    class BoxCountItem(
        val count: Int,
        private val optionTitle: String
    ) {
        override fun toString(): String {
            return optionTitle
        }
    }

}