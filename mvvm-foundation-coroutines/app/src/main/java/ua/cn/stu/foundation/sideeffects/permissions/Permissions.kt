package ua.cn.stu.foundation.sideeffects.permissions

import ua.cn.stu.foundation.sideeffects.permissions.plugin.PermissionStatus
import ua.cn.stu.foundation.sideeffects.permissions.plugin.PermissionsPlugin

/**
 * Side-effects interface for managing permissions from view-model.
 * You need to add [PermissionsPlugin] to your activity before using this feature.
 *
 * WARNING! Please note that such usage of permissions requests doesn't allow you to handle
 * responses after app killing.
 */
interface Permissions {

    /**
     * Whether the app has the specified permission or not.
     */
    fun hasPermissions(permission: String): Boolean

    /**
     * Request the specified permission.
     * See [PermissionStatus]
     */
    suspend fun requestPermission(permission: String): PermissionStatus

}