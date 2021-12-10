package ua.cn.stu.room.model.room

import androidx.room.RoomDatabase

// todo #8: Create a database class by extending RoomDatabase.
//          - annotate this class with @Database annotation
//          - use 'version' parameter to specify database scheme version (should be version=1)
//          - use 'entities' parameter to list all entities (classes annotated with @Entity)
//          - use 'views' parameter to list all views (classes annotated with @DatabaseView)
abstract class AppDatabase : RoomDatabase() {

    // todo #10: Add abstract getAccountsDao() method

    // todo #18: Add abstract getBoxesDao() method

}