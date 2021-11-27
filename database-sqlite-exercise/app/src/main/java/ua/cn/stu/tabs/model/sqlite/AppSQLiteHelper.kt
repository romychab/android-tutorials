package ua.cn.stu.tabs.model.sqlite

import android.content.Context

class AppSQLiteHelper(
    private val applicationContext: Context
) {

    // TODO #1
    //       1) extend this class from SQLiteOpenHelper (use any name and version = 1)
    //       2) implement onCreate method:
    //          - open db_init.sql from assets
    //          - split SQL instruction by ';' char
    //          - filter empty SQL instructions
    //          - execute each instruction by using SQLiteDatabase.execSQL() method
    //       3) leave onUpgrade() method empty


}