package ua.cn.stu.paging.model

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.cn.stu.paging.model.users.repositories.room.UserDbEntity
import ua.cn.stu.paging.model.users.repositories.room.UsersDao

@Database(
    version = 1,
    entities = [
        UserDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUsersDao(): UsersDao

}
