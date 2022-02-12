package ua.cn.stu.paging.model.users.repositories.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update

@Dao
interface UsersDao {

    @Query("SELECT * FROM users " +
            "WHERE :searchBy = '' OR name LIKE '%' || :searchBy || '%' " + // search substring
            "ORDER BY name " +  // sort by user name
            "LIMIT :limit OFFSET :offset") // return max :limit number of users starting from :offset position
    suspend fun getUsers(limit: Int, offset: Int, searchBy: String = ""): List<UserDbEntity>

    @Update(entity = UserDbEntity::class)
    suspend fun setIsFavorite(tuple: UpdateUserFavoriteFlagTuple)

    @Delete(entity = UserDbEntity::class)
    suspend fun delete(id: IdTuple)

}