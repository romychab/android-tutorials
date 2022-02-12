package ua.cn.stu.paging.model.users.repositories.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ua.cn.stu.paging.model.users.User

@Entity(
    tableName = "users",
    indices = [
        Index("name")
    ]
)
data class UserDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
    val company: String,
    val imageUrl: String,
    val isFavorite: Boolean
) {

    fun toUser(): User = User(
        id = id,
        name = name,
        company = company,
        imageUrl = imageUrl,
        isFavorite = isFavorite
    )

}