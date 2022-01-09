package ua.cn.stu.room.model.accounts.room.entities

import androidx.room.*
import ua.cn.stu.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ua.cn.stu.room.model.boxes.room.entities.BoxDbEntity
import ua.cn.stu.room.model.boxes.room.views.SettingDbView

/**
 * Fetch only ID and password from 'accounts' table.
 */
data class AccountSignInTuple(
    @ColumnInfo(name = "id") val id: Long,
    // todo #5: do not forget to replace 'password' by 'hash' + 'salt' in the tuple class here:
    @ColumnInfo(name = "password") val password: String
)

/**
 * Tuple for updating username by account id
 */
data class AccountUpdateUsernameTuple(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String
)

/**
 * Tuple used for querying account data + boxes with edited settings.
 */
data class AccountAndEditedBoxesTuple(
    @Embedded val accountDbEntity: AccountDbEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value =  AccountBoxSettingDbEntity::class,
            parentColumn = "account_id",
            entityColumn = "box_id"
        )
    )
    val boxes: List<BoxDbEntity>
)

/**
 * Tuple for querying account data + settings for all boxes + boxes data
 */
data class AccountAndAllSettingsTuple(
    @Embedded val accountDbEntity: AccountDbEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "account_id",
        entity = SettingDbView::class
    )
    val settings: List<SettingAndBoxTuple>
)

/**
 * Helper tuple class for [AccountAndAllSettingsTuple] with nested relation.
 */
data class SettingAndBoxTuple(
    @Embedded val accountBoxSettingsDbEntity: SettingDbView,
    @Relation(
        parentColumn = "box_id",
        entityColumn = "id"
    )
    val boxDbEntity: BoxDbEntity
)