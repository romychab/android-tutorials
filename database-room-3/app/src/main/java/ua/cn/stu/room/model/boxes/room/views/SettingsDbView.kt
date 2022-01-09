package ua.cn.stu.room.model.boxes.room.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import ua.cn.stu.room.model.boxes.room.entities.SettingsTuple

/**
 * This database view looks like 'accounts_boxes_settings' table but
 * returns default ('1') values for rows which don't exist in 'accounts_boxes_settings'.
 */
@DatabaseView(
    viewName = "settings_view",
    value = "SELECT \n" +
            "  accounts.id as account_id, \n" +
            "  boxes.id as box_id,\n" +
            "  ifnull(accounts_boxes_settings.is_active, 1) as is_active\n" +
            "FROM accounts\n" +
            "JOIN boxes\n" +
            "LEFT JOIN accounts_boxes_settings\n" +
            "  ON accounts_boxes_settings.account_id = accounts.id \n" +
            "    AND accounts_boxes_settings.box_id = boxes.id"
)
data class SettingDbView(
    @ColumnInfo(name = "account_id") val accountId: Long,
    @ColumnInfo(name = "box_id") val boxId: Long,
    @Embedded val settings: SettingsTuple,
)
