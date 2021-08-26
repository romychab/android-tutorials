package ua.cn.stu.foundation.sideeffects

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Base class for side-effect implementations.
 * Implementations are responsible for the real implementation of side-effects taking into
 * account activity lifecycle. You may safely launch dialogs, perform navigation, change UI here.
 * Implementations are tied to activity.
 */
abstract class SideEffectImplementation {

    private lateinit var activity: AppCompatActivity

    fun requireActivity(): AppCompatActivity = activity

    open fun onCreate(savedInstanceState: Bundle?) {}
    open fun onBackPressed(): Boolean { return false }
    open fun onRequestUpdates() {}
    open fun onSupportNavigateUp(): Boolean? {
        return null
    }
    open fun onSaveInstanceState(outBundle: Bundle) {}
    open fun onActivityResult(requestCode: Int, responseCode: Int, data: Intent?) {}
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, granted: IntArray) {}

    internal fun injectActivity(activity: AppCompatActivity) {
        this.activity = activity
    }

}