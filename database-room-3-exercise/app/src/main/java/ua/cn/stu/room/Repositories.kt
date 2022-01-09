package ua.cn.stu.room

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ua.cn.stu.room.model.accounts.AccountsRepository
import ua.cn.stu.room.model.accounts.room.RoomAccountsRepository
import ua.cn.stu.room.model.boxes.BoxesRepository
import ua.cn.stu.room.model.boxes.room.RoomBoxesRepository
import ua.cn.stu.room.model.room.AppDatabase
import ua.cn.stu.room.model.settings.AppSettings
import ua.cn.stu.room.model.settings.SharedPreferencesAppSettings
import ua.cn.stu.room.utils.security.DefaultSecurityUtilsImpl
import ua.cn.stu.room.utils.security.SecurityUtils

object Repositories {

    private lateinit var applicationContext: Context

    // -- stuffs

    val securityUtils: SecurityUtils by lazy { DefaultSecurityUtilsImpl() }

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            // todo #17: use addMigrations method to add your manual migration from 2nd to 3rd DB version
            .createFromAsset("initial_database.db")
            .build()
    }

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private val appSettings: AppSettings by lazy {
        SharedPreferencesAppSettings(applicationContext)
    }

    // --- repositories

    val accountsRepository: AccountsRepository by lazy {
        RoomAccountsRepository(database.getAccountsDao(), appSettings, securityUtils, ioDispatcher)
    }

    val boxesRepository: BoxesRepository by lazy {
        RoomBoxesRepository(accountsRepository, database.getBoxesDao(), ioDispatcher)
    }

    /**
     * Call this method in all application components that may be created at app startup/restoring
     * (e.g. in onCreate of activities and services)
     */
    fun init(context: Context) {
        applicationContext = context
    }
}