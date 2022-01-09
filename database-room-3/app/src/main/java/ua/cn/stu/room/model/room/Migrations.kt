package ua.cn.stu.room.model.room

import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import ua.cn.stu.room.Repositories

@RenameColumn(tableName = "accounts", fromColumnName = "password", toColumnName = "hash")
class AutoMigrationSpec1To2 : AutoMigrationSpec {

    private val securityUtils = Repositories.securityUtils

    override fun onPostMigrate(db: SupportSQLiteDatabase) {
        super.onPostMigrate(db)
        db.query("SELECT * FROM accounts").use { cursor ->
            val passwordIndex = cursor.getColumnIndex("hash")
            val idIndex = cursor.getColumnIndex("id")
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idIndex)
                val passwordChars = cursor.getString(passwordIndex).toCharArray()
                val salt = securityUtils.generateSalt()
                val hashBytes = securityUtils.passwordToHash(passwordChars, salt)

                db.update(
                    "accounts",
                    SQLiteDatabase.CONFLICT_NONE,
                    contentValuesOf(
                        "hash" to securityUtils.bytesToString(hashBytes),
                        "salt" to securityUtils.bytesToString(salt)
                    ),
                    "id = ?",
                    arrayOf(id.toString())
                )
            }
        }
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE accounts ADD phone TEXT")
    }
}
