package ua.cn.stu.room.model.accounts.entities

import ua.cn.stu.room.model.boxes.entities.BoxAndSettings

/**
 * Account info with all boxes and their settings
 */
data class AccountFullData(
    val account: Account,
    val boxesAndSettings: List<BoxAndSettings>
)