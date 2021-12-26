package ua.cn.stu.room.model.accounts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import ua.cn.stu.room.model.accounts.room.entities.AccountDbEntity
import ua.cn.stu.room.model.accounts.room.entities.AccountSignInTuple
import ua.cn.stu.room.model.accounts.room.entities.AccountUpdateUsernameTuple

@Dao
interface AccountsDao {
    @Query("SELECT id, password FROM accounts WHERE email = :email")
    suspend fun findByEmail(email: String): AccountSignInTuple?

    @Update(entity = AccountDbEntity::class)
    suspend fun updateUsername(account: AccountUpdateUsernameTuple)

    @Insert(entity = AccountDbEntity::class)
    suspend fun createAccount(accountDbEntity: AccountDbEntity)

    @Query("SELECT * FROM accounts WHERE id = :accountId")
    fun getById(accountId: Long): Flow<AccountDbEntity?>

    // todo #18: Add a method for fetching boxes with edited settings (AccountAndEditedBoxesTuple)
    //           by account id

    // todo #20: Add a method for fetching all data from the database: all accounts, their settings
    //           and all related boxes.
}
