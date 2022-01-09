package ua.cn.stu.room.model.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import ua.cn.stu.room.model.accounts.room.AccountsDao
import ua.cn.stu.room.model.accounts.room.entities.AccountDbEntity
import ua.cn.stu.room.model.boxes.room.BoxesDao
import ua.cn.stu.room.model.boxes.room.entities.AccountBoxSettingDbEntity
import ua.cn.stu.room.model.boxes.room.entities.BoxDbEntity
import ua.cn.stu.room.model.boxes.room.views.SettingDbView

@Database(
    version = 3,
    entities = [
        AccountDbEntity::class,
        BoxDbEntity::class,
        AccountBoxSettingDbEntity::class
    ],
    views = [
        SettingDbView::class
    ],
    autoMigrations = [
        AutoMigration(
            from = 1,
            to = 2,
            spec = AutoMigrationSpec1To2::class
        )
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAccountsDao(): AccountsDao

    abstract fun getBoxesDao(): BoxesDao

}