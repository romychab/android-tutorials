package ua.cn.stu.room.model.accounts.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ua.cn.stu.room.model.accounts.entities.Account
import ua.cn.stu.room.model.accounts.entities.SignUpData
import ua.cn.stu.room.utils.security.SecurityUtils

@Entity(
    tableName = "accounts",
    indices = [
        Index("email", unique = true)
    ]
)
data class AccountDbEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "email", collate = ColumnInfo.NOCASE) val email: String,
    @ColumnInfo(name = "username") val username: String,
    // todo #3: rename 'password' column to 'hash' and also add a new 'salt' column
    @ColumnInfo(name = "password") val password: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
    // todo #14: add 'phone' column
) {

    fun toAccount(): Account = Account(
        id = id,
        email = email,
        username = username,
        createdAt = createdAt
    )

    companion object {
        fun fromSignUpData(signUpData: SignUpData, securityUtils: SecurityUtils): AccountDbEntity {
            // todo #4: Use SecurityUtils to generate a random salt and to hash the password
            //          coming in SignUpData. Also it's a good practice to clear password variables
            //          after usage (e.g. fill them with '*' char).
            //          Then assign the generated salt and the calculated hash to the AccountDbEntity properties.
            return AccountDbEntity(
                id = 0, // SQLite generates identifier automatically if ID = 0
                email = signUpData.email,
                username = signUpData.username,
                password = String(signUpData.password),
                createdAt = System.currentTimeMillis()
                // todo #15: fill 'phone' column with 'NULL' by default
            )
        }
    }
}
