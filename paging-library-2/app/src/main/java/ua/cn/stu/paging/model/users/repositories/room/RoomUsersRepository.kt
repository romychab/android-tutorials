package ua.cn.stu.paging.model.users.repositories.room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import ua.cn.stu.paging.model.users.User
import ua.cn.stu.paging.model.users.UsersPageLoader
import ua.cn.stu.paging.model.users.UsersPagingSource
import ua.cn.stu.paging.model.users.repositories.UsersRepository

class RoomUsersRepository(
    private val ioDispatcher: CoroutineDispatcher,
    private val usersDao: UsersDao
) : UsersRepository {

    private val enableErrorsFlow = MutableStateFlow(false)

    override fun isErrorsEnabled(): Flow<Boolean> = enableErrorsFlow

    override fun setErrorsEnabled(value: Boolean) {
        enableErrorsFlow.value = value
    }

    override fun getPagedUsers(searchBy: String): Flow<PagingData<User>> {
        val loader: UsersPageLoader = { pageIndex, pageSize ->
            getUsers(pageIndex, pageSize, searchBy)
        }
        return Pager(
            config = PagingConfig(
                // for now let's use the same page size for initial
                // and subsequent loads
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE,
                prefetchDistance = PAGE_SIZE / 2,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(loader) }
        ).flow
    }

    override suspend fun setIsFavorite(user: User, isFavorite: Boolean) = withContext(ioDispatcher) {
        delay(1000)
        throwErrorsIfEnabled()

        val tuple = UpdateUserFavoriteFlagTuple(user.id, isFavorite)
        usersDao.setIsFavorite(tuple)
    }

    override suspend fun delete(user: User) = withContext(ioDispatcher) {
        delay(1000)
        throwErrorsIfEnabled()

        usersDao.delete(IdTuple(user.id))
    }

    private suspend fun getUsers(pageIndex: Int, pageSize: Int, searchBy: String): List<User>
    = withContext(ioDispatcher) {

        delay(2000) // some delay to test loading state
        throwErrorsIfEnabled()

        // calculate offset value required by DAO
        val offset = pageIndex * pageSize

        // get page
        val list = usersDao.getUsers(pageSize, offset, searchBy)

        // map UserDbEntity to User
        return@withContext list
            .map(UserDbEntity::toUser)
    }

    private fun throwErrorsIfEnabled() {
        if (enableErrorsFlow.value) throw IllegalStateException("Error!")
    }

    private companion object {
        const val PAGE_SIZE = 40
    }
}