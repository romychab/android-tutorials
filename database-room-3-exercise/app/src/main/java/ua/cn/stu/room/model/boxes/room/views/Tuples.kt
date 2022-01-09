package ua.cn.stu.room.model.boxes.room.views

import androidx.room.Embedded
import androidx.room.Relation
import ua.cn.stu.room.model.accounts.room.entities.AccountDbEntity
import ua.cn.stu.room.model.boxes.room.entities.BoxDbEntity

/**
 * Tuple for querying rows from 'settings_view' with related entities:
 * [AccountDbEntity] by value in the 'account_id' column and [BoxDbEntity] by
 * value in the 'box_id' column.
 */
data class SettingWithEntitiesTuple(
    @Embedded val settingDbEntity: SettingDbView,

    @Relation(
        parentColumn = "account_id",
        entityColumn = "id"
    )
    val accountDbEntity: AccountDbEntity,

    @Relation(
        parentColumn = "box_id",
        entityColumn = "id"
    )
    val boxDbEntity: BoxDbEntity
)