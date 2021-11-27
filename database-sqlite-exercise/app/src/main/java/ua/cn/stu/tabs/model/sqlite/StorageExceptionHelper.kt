package ua.cn.stu.tabs.model.sqlite

import android.database.sqlite.SQLiteException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import ua.cn.stu.tabs.model.StorageException

/**
 * Converts any [SQLiteException] into in-app [StorageException]
 */
suspend fun <T> wrapSQLiteException(dispatcher: CoroutineDispatcher, block: suspend CoroutineScope.() -> T): T {
    try {
        return withContext(dispatcher, block)
    } catch (e: SQLiteException) {
        val appException = StorageException()
        appException.initCause(e)
        throw appException
    }
}