package ua.cn.stu.room.model.boxes.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded

/**
 * Tuple containing only setting itself without IDs.
 */
data class SettingsTuple(
    @ColumnInfo(name = "is_active") val isActive: Boolean
)

/**
 * Tuple for joining box data and settings data.
 */
data class BoxAndSettingsTuple(
    @Embedded val boxDbEntity: BoxDbEntity,
    @Embedded val settingDbEntity: AccountBoxSettingDbEntity?
)