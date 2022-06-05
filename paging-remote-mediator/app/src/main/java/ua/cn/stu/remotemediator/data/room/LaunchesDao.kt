package ua.cn.stu.remotemediator.data.room

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface LaunchesDao {

    /**
     * Note that orderBy and ASC/DESC order should be the same as
     * in the network request.
     */
    @Query("SELECT * FROM launches WHERE :year IS NULL OR year = :year ORDER BY launchTimestamp DESC")
    fun getPagingSource(
        year: Int?
    ): PagingSource<Int, LaunchRoomEntity>

    /**
     * Insert (or replace by ID) a list of SpaceX Launches.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(launches: List<LaunchRoomEntity>)

    /**
     * Clear local records for the specified year (or clear all
     * local records if year is NULL)
     */
    @Query("DELETE FROM launches WHERE :year IS NULL OR year = :year")
    suspend fun clear(year: Int?)

    /**
     * Clear old records and place new records from the list.
     */
    @Transaction
    suspend fun refresh(year: Int?, launches: List<LaunchRoomEntity>) {
        clear(year)
        save(launches)
    }

    /**
     * Convenient call for saving one Launch entity.
     */
    suspend fun save(launch: LaunchRoomEntity) {
        save(listOf(launch))
    }
}