package ua.cn.stu.room.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class AccountSignInTuple(
    @ColumnInfo(name = "id") val id: Long,
    @ColumnInfo(name = "password") val password: String
)

data class AccountUpdateUsernameTuple(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "username") val username: String
)

// todo #17: Create an AccountAndEditedBoxesTuple class which joins queries all boxes which settings have
//           been edited for the specified account.
//           Hint: use @Relation annotation with 'associateBy' argument.

// todo #19: Create an AccountAndAllSettingsTuple + SettingAndBoxTuple classes (hint: both of them with
//           @Relation annotation). AccountAndAllSettingsTuple should fetch account data from 'accounts'
//           table and also should contain a reference to the SettingAndBoxTuple. The latter should
//           contain data from 'SettingDbView' ('is_active' flag) and also it should contain a reference
//           to the BoxDbEntity (data from 'boxes' table).