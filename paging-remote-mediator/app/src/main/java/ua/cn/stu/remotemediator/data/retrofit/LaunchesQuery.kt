package ua.cn.stu.remotemediator.data.retrofit

import com.google.gson.annotations.SerializedName
import java.util.*
import java.util.concurrent.TimeUnit

data class LaunchesQuery(
    val query: QueryFilter?,
    val options: QueryOptions
) {
    companion object {
        /**
         * Method for simpler creation of [LaunchesQuery] instance.
         */
        fun create(year: Int?, offset: Int, limit: Int): LaunchesQuery {
            val query = year?.let {
                QueryFilter(dateUnix = yearToUnixTimestampRange(it))
            }
            return LaunchesQuery(
                query = query,
                options = QueryOptions(
                    offset = offset,
                    limit = limit
                )
            )

        }

        private fun yearToUnixTimestampRange(year: Int): QueryRange {
            val calendar = Calendar.getInstance()
            calendar.setYear(year)
            val start = calendar.getUnixTimestamp()
            calendar.setYear(year + 1)
            val end = calendar.getUnixTimestamp()
            return QueryRange(
                greaterOrEqualsThan = start,
                lessThan = end
            )
        }

        private fun Calendar.setYear(year: Int) {
            set(year, Calendar.JANUARY, 1, 0, 0, 0)
        }

        private fun Calendar.getUnixTimestamp(): Long {
            return TimeUnit.MILLISECONDS.toSeconds(timeInMillis)
        }
    }

}

data class QueryOptions(
    val offset: Int,
    val limit: Int,
    // sort by date, desc order
    val sort: String = "-date_unix",
    // fields to be fetched
    val select: String = "flight_number name details links date_unix success"
)

data class QueryFilter(
    val dateUnix: QueryRange
)

data class QueryRange(
    @SerializedName("\$gte") val greaterOrEqualsThan: Long,
    @SerializedName("\$lt") val lessThan: Long
)
