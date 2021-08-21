package ua.cn.stu.foundation.sideeffects.permissions.plugin

enum class PermissionStatus {
    /**
     * App can safely use features that require permission
     */
    GRANTED,

    /**
     * App doesn't have permission
     */
    DENIED,

    /**
     * App doesn't have permission and user has chosen "Don't ask again" option in the system dialog.
     */
    DENIED_FOREVER
}