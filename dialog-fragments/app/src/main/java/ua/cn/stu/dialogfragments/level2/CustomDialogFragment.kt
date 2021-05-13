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
import ua.cn.stu.dialogfragments.databinding.PartVolumeBinding

class CustomDialogFragment : DialogFragment() {

    private val volume: Int
        get() = requireArguments().getInt(ARG_VOLUME)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBinding = PartVolumeBinding.inflate(layoutInflater)
        dialogBinding.volumeSeekBar.progress = volume
        return AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setTitle(R.string.volume_setup)
                .setMessage(R.string.volume_setup_message)
                .setView(dialogBinding.root)
                .setPositiveButton(R.string.action_confirm) { _, _ ->
                    val newVolume = dialogBinding.volumeSeekBar.progress
                    parentFragmentManager.setFragmentResult(REQUEST_KEY, bundleOf(KEY_VOLUME_RESPONSE to newVolume))
                }
                .create()
    }

    companion object {
        @JvmStatic private val TAG = CustomDialogFragment::class.java.simpleName
        @JvmStatic private val KEY_VOLUME_RESPONSE = "KEY_VOLUME_RESPONSE"
        @JvmStatic private val ARG_VOLUME = "ARG_VOLUME"

        @JvmStatic val REQUEST_KEY = "$TAG:defaultRequestKey"

        fun show(manager: FragmentManager, volume: Int) {
            val dialogFragment = CustomDialogFragment()
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