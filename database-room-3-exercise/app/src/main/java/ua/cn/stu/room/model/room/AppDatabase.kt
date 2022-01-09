package ua.cn.stu.room.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.cn.stu.room.model.accounts.room.AccountsDao
import ua.cn.stu.room.model.accounts.room.entities.AccountDbEntity
import ua.cn.stu.room.model.boxes.room.BoxesDao
import ua.cn.stu.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ua.cn.stu.room.model.boxes.room.entities.BoxDbEntity
import ua.cn.stu.room.model.boxes.room.views.SettingDbView

@Database(
    version = 1,    // todo #1: install the app to your device/emulator; then increment DB version by 1: from 1 to 2.
                    // todo #13: now let's increment DB version again by 1: from 2 to 3; we will
                    //           add 'phone' column to the 'accounts' table by hands
    entities = [
        AccountDbEntity::class,
        BoxDbEntity::class,
        AccountBoxSettingDbEntity::class
    ],
    views = [
        SettingDbView::class
    ]
    // todo #2: add autoMigration argument for auto-migrating from the 1st to the 2nd DB version

    // todo #9: specify 'spec' argument in the AutoMigration and assign your spec class.
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao

}