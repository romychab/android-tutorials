package ua.cn.stu.remotemediator.data.retrofit

import com.google.gson.annotations.SerializedName
import ua.cn.stu.remotemediator.domain.Launch
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Network entity of SpaceX Launch. This structure differs from the [Launch]
 * interface so some properties are overridden in the class body, not in the
 * constructor:
 * - Constructor of this data class specifies the network JSON structure.
 * - All properties outside constructor are overridden properties of the [Launch] interface
 */
data class LaunchNetworkEntity(
    @SerializedName("flight_number") override val id: Long,
    override val name: String,
    val details: String?,
    val links: Links?,
    val dateUnix: Long,
    val success: Boolean
) : Launch {

    override val description: String get() = details ?: "-"
    override val isSuccess: Boolean get() = success
    override val imageUrl: String get() = links?.patch?.small ?: ""
    override val launchTimestamp: Long get() = dateUnix

    override val year: Int get() = Calendar.getInstance().apply {
        timeInMillis = TimeUnit.SECONDS.toMillis(launchTimestamp)
    }.get(Calendar.YEAR)

}

data class Links(
    val patch: Images?,
)

data class Images(
    val small: String?
)
