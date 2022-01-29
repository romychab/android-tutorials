package ua.cn.stu.paging.model.users.repositories.room

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UsersPagingSource(loader, PAGE_SIZE) }
        ).flow
    }

    private suspend fun getUsers(pageIndex: Int, pageSize: Int, searchBy: String): List<User>
    = withContext(ioDispatcher) {

        delay(2000) // some delay to test loading state

        // if "Enable Errors" checkbox is checked -> throw exception
        if (enableErrorsFlow.value) throw IllegalStateException("Error!")

        // calculate offset value required by DAO
        val offset = pageIndex * pageSize

        // get page
        val list = usersDao.getUsers(pageSize, offset, searchBy)

        // map UserDbEntity to User
        return@withContext list
            .map(UserDbEntity::toUser)
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}