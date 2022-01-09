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
    @ColumnInfo(name = "hash") val hash: String,
    @ColumnInfo(name = "salt", defaultValue = "") val salt: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "phone") val phone: String?,
) {

    fun toAccount(): Account = Account(
        id = id,
        email = email,
        username = username,
        createdAt = createdAt
    )

    companion object {
        fun fromSignUpData(signUpData: SignUpData, securityUtils: SecurityUtils): AccountDbEntity {
            val salt = securityUtils.generateSalt()
            val hash = securityUtils.passwordToHash(signUpData.password, salt)
            signUpData.password.fill('*')
            signUpData.repeatPassword.fill('*')
            return AccountDbEntity(
                id = 0, // SQLite generates identifier automatically if ID = 0
                email = signUpData.email,
                username = signUpData.username,
                hash = securityUtils.bytesToString(hash),
                salt = securityUtils.bytesToString(salt),
                createdAt = System.currentTimeMillis(),
                phone = null
            )
        }
    }
}
