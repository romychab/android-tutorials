package ua.cn.stu.foundation.sideeffects.permissions.plugin

import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import ua.cn.stu.foundation.model.SuccessResult
import ua.cn.stu.foundation.sideeffects.SideEffectImplementation
import ua.cn.stu.foundation.sideeffects.permissions.plugin.PermissionsSideEffectMediator.RetainedState

class PermissionsSideEffectImpl(
    private val retainedState: RetainedState
) : SideEffectImplementation() {

    fun requestPermission(permission: String) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        granted: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, granted)
        val emitter = retainedState.emitter ?: return
        if (requestCode == REQUEST_CODE) {
            retainedState.emitter = null
            if (granted[0] == PackageManager.PERMISSION_GRANTED) {
                emitter.emit(SuccessResult(PermissionStatus.GRANTED))
            } else {
                val showRationale = requireActivity().shouldShowRequestPermissionRationale(permissions[0])
                if (showRationale) {
                    emitter.emit(SuccessResult(PermissionStatus.DENIED))
                } else {
                    emitter.emit(SuccessResult(PermissionStatus.DENIED_FOREVER))
                }
            }
        }
    }

    private companion object {
        const val REQUEST_CODE = 1100
    }
}