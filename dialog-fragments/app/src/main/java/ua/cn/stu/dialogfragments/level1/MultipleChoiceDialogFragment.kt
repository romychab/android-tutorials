package ua.cn.stu.dialogfragments.level1

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import ua.cn.stu.dialogfragments.R

class MultipleChoiceDialogFragment : DialogFragment() {

    private val color: Int
        get() = requireArguments().getInt(ARG_COLOR)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val colorItems = resources.getStringArray(R.array.colors)
        val colorComponents = mutableListOf(
            Color.red(this.color),
            Color.green(this.color),
            Color.blue(this.color)
        )
        val checkboxes = colorComponents
            .map { it > 0 && savedInstanceState == null }
            .toBooleanArray()

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.volume_setup)
            .setMultiChoiceItems(colorItems, checkboxes) { dialog, _, _ ->
                // NEW:
                val checkedPositions = (dialog as AlertDialog).listView.checkedItemPositions
                val color = Color.rgb(
                    booleanToColorComponent(checkedPositions[0]),
                    booleanToColorComponent(checkedPositions[1]),
                    booleanToColorComponent(checkedPositions[2])
                )
                parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_COLOR_RESPONSE to color))
            }
            .setPositiveButton(R.string.action_close, null)
            .create()
    }

    private fun booleanToColorComponent(value: Boolean): Int {
        return if (value) 255 else 0
    }

    companion object {
        @JvmStatic private val TAG = MultipleChoiceDialogFragment::class.java.simpleName
        @JvmStatic private val KEY_COLOR_RESPONSE = "KEY_COLOR_RESPONSE"
        @JvmStatic private val ARG_COLOR = "ARG_COLOR"

        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, color: Int) {
            val dialogFragment = MultipleChoiceDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_COLOR to color)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_COLOR_RESPONSE))
            })
        }

    }
}