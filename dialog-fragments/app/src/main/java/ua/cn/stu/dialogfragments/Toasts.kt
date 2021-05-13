package ua.cn.stu.dialogfragments

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment

fun DialogFragment.showToast(@StringRes messageRes: Int) {
    showToast(requireContext(), messageRes)
}

fun AppCompatActivity.showToast(@StringRes messageRes: Int) {
    showToast(this, messageRes)
}

private fun showToast(context: Context, @StringRes messageRes: Int) {
    Toast.makeText(context, messageRes, Toast.LENGTH_SHORT).show()
}