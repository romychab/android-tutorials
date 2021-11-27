package ua.cn.stu.tabs

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.cn.stu.tabs.model.accounts.AccountsRepository
import ua.cn.stu.tabs.model.accounts.SQLiteAccountsRepository
import ua.cn.stu.tabs.model.boxes.BoxesRepository
import ua.cn.stu.tabs.model.boxes.SQLiteBoxesRepository
import ua.cn.stu.tabs.model.settings.AppSettings
import ua.cn.stu.tabs.model.settings.SharedPreferencesAppSettings
import ua.cn.stu.tabs.model.sqlite.AppSQLiteHelper

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    private val database: SQLiteDatabase by lazy<SQLiteDatabase> {
        AppSQLiteHelper(applicationContext).writableDatabase
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        SQLiteAccountsRepository(database, appSettings, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        SQLiteBoxesRepository(database, accountsRepository, ioDispatcher)
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}