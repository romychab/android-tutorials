package ua.cn.stu.remotemediator.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ua.cn.stu.remotemediator.data.retrofit.LaunchesApi
import ua.cn.stu.remotemediator.data.retrofit.LaunchesQuery
import ua.cn.stu.remotemediator.data.room.LaunchRoomEntity
import ua.cn.stu.remotemediator.data.room.LaunchesDao

@ExperimentalPagingApi
class LaunchesRemoteMediator @AssistedInject constructor(
    private val launchesDao: LaunchesDao,
    private val launchesApi: LaunchesApi,
    @Assisted private val year: Int?
) : RemoteMediator<Int, LaunchRoomEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LaunchRoomEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
            return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val launches = fetchLaunches(limit, offset)
            if (loadType == LoadType.REFRESH) {
                launchesDao.refresh(year, launches)
            } else {
                launchesDao.save(launches)
            }
            MediatorResult.Success(
                endOfPaginationReached = launches.size < limit
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return null
            LoadType.APPEND -> ++pageIndex
        }
        return pageIndex
    }

    private suspend fun fetchLaunches(
        limit: Int,
        offset: Int
    ): List<LaunchRoomEntity> {
        val query = LaunchesQuery.create(
            year = year,
            limit = limit,
            offset = offset
        )
        return launchesApi.getLaunches(query)
            .docs
            .map { LaunchRoomEntity(it) }
    }

    @AssistedFactory
    interface Factory {
        fun create(year: Int?): LaunchesRemoteMediator
    }

}