package ua.cn.stu.room.model.boxes.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ua.cn.stu.room.model.boxes.room.entities.BoxDbEntity

@Dao
interface BoxesDao {

    @Query("SELECT * " +
            "FROM boxes " +
            "LEFT JOIN accounts_boxes_settings " +
            "  ON boxes.id = accounts_boxes_settings.box_id AND accounts_boxes_settings.account_id = :accountId")
    fun getBoxesAndSettings(accountId: Long): Flow<Map<BoxDbEntity, AccountBoxSettingDbEntity?>>

    // todo #6: Rewrite the getBoxesAndSettings method: use BoxAndSettingsTuple instead of Map.

    // todo #11: Rewrite getBoxesAndSettings method again: use database view instead of complex SQL-query.

    // todo #15: Rewrite getBoxesAndSettings method again: Use SettingWithEntitiesTuple which
    //           represents joined data from the database view, 'accounts' and 'boxes' tables

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setActiveFlagForBox(accountBoxSetting: AccountBoxSettingDbEntity)

}
