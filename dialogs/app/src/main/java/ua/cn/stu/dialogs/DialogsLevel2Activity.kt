package ua.cn.stu.dialogs

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import ua.cn.stu.dialogs.databinding.ActivityLevel2Binding
import ua.cn.stu.dialogs.databinding.PartVolumeBinding
import ua.cn.stu.dialogs.databinding.PartVolumeInputBinding
import ua.cn.stu.dialogs.entities.AvailableVolumeValues
import kotlin.properties.Delegates

class DialogsLevel2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLevel2Binding
    private var volume by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevel2Binding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.showCustomAlertDialogButton.setOnClickListener {
            showCustomAlertDialog()
        }
        binding.showCustomSingleChoiceAlertDialogButton.setOnClickListener {
            showCustomSingleChoiceAlertDialog()
        }
        binding.showInputAlertDialogButton.setOnClickListener {
            showCustomInputAlertDialog()
        }

        volume = savedInstanceState?.getInt(KEY_VOLUME) ?: 50
        updateUi()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_VOLUME, volume)
    }

    private fun showCustomAlertDialog() {
        val dialogBinding = PartVolumeBinding.inflate(layoutInflater)
        dialogBinding.volumeSeekBar.progress = volume
        val dialog = AlertDialog.Builder(this)
                .setCancelable(true)
                .setTitle(R.string.volume_setup)
                .setMessage(R.string.volume_setup_message)
                .setView(dialogBinding.root)
                .setPositiveButton(R.string.action_confirm) { _, _ ->
                    volume = dialogBinding.volumeSeekBar.progress
                    updateUi()
                }
                .create()
        dialog.show()
    }

    private fun showCustomSingleChoiceAlertDialog() {
        val volumeItems = AvailableVolumeValues.createVolumeValues(volume)
        val adapter = VolumeAdapter(volumeItems.values)

        var volume = this.volume
        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.volume_setup)
                .setSingleChoiceItems(adapter, volumeItems.currentIndex) { _, which ->
                    volume = adapter.getItem(which)
                }
                .setPositiveButton(R.string.action_confirm) { _, _ ->
                    this.volume = volume
                    updateUi()
                }
                .create()
        dialog.show()
    }

    private fun showCustomInputAlertDialog() {
        val dialogBinding = PartVolumeInputBinding.inflate(layoutInflater)
        dialogBinding.volumeInputEditText.setText(volume.toString())

        val dialog = AlertDialog.Builder(this)
                .setTitle(R.string.volume_setup)
                .setView(dialogBinding.root)
                .setPositiveButton(R.string.action_confirm, null)
                .create()
        dialog.setOnShowListener {
            dialogBinding.volumeInputEditText.requestFocus()
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
                val enteredText = dialogBinding.volumeInputEditText.text.toString()
                if (enteredText.isBlank()) {
                    dialogBinding.volumeInputEditText.error = getString(R.string.empty_value)
                    return@setOnClickListener
                }
                val volume = enteredText.toIntOrNull()
                if (volume == null || volume > 100) {
                    dialogBinding.volumeInputEditText.error = getString(R.string.invalid_value)
                    return@setOnClickListener
                }
                this.volume = volume
                updateUi()
                dialog.dismiss()
            }
        }
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.show()
    }

    private fun updateUi() {
        binding.currentVolumeTextView.text = getString(R.string.current_volume, volume)
    }

    companion object {
        @JvmStatic private val TAG = DialogsLevel2Activity::class.java.simpleName
        @JvmStatic private val KEY_VOLUME = "KEY_VOLUME"
    }

}