package ua.cn.stu.dialogfragments.level2

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import ua.cn.stu.dialogfragments.R
import ua.cn.stu.dialogfragments.VolumeAdapter
import ua.cn.stu.dialogfragments.entities.AvailableVolumeValues

class CustomSingleChoiceDialogFragment : DialogFragment() {

    private val volume: Int
        get() = requireArguments().getInt(ARG_VOLUME)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val adapter = VolumeAdapter(volumeItems.values)
        return AlertDialog.Builder(requireContext())
                .setTitle(R.string.volume_setup)
                .setSingleChoiceItems(adapter, volumeItems.currentIndex, null)
                .setPositiveButton(R.string.action_confirm) { dialog, _ ->
                    val index = (dialog as AlertDialog).listView.checkedItemPosition
                    val volume = volumeItems.values[index]
                    parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_VOLUME_RESPONSE to volume))
                }
                .create()
    }

    companion object {
        @JvmStatic private val TAG = CustomSingleChoiceDialogFragment::class.java.simpleName
        @JvmStatic private val KEY_VOLUME_RESPONSE = "KEY_VOLUME_RESPONSE"
        @JvmStatic private val ARG_VOLUME = "ARG_VOLUME"

        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, volume: Int) {
            val dialogFragment = CustomSingleChoiceDialogFragment()
            dialogFragment.arguments = bundleOf(ARG_VOLUME to volume)
            dialogFragment.show(manager, TAG)
        }

        fun setupListener(manager: FragmentManager, lifecycleOwner: LifecycleOwner, listener: (Int) -> Unit) {
            manager.setFragmentResultListener(REQUEST_KEY, lifecycleOwner, FragmentResultListener { _, result ->
                listener.invoke(result.getInt(KEY_VOLUME_RESPONSE))
            })
        }

    }
}