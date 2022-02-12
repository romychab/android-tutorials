package ua.cn.stu.paging.model.users.repositories.room

/**
 * Tuple for updating favorite ("star") flag for the user with the specified ID.
 */
data class UpdateUserFavoriteFlagTuple(
    val id: Long,
    val isFavorite: Boolean
)

/**
 * Tuple for deleting user by ID.
 */
data class IdTuple(
    val id: Long
)