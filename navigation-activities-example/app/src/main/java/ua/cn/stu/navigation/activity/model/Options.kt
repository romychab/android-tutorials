package ua.cn.stu.navigation.activity.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val boxCount: Int,
    val isTimerEnabled: Boolean
) : Parcelable {

    companion object {
        @JvmStatic val DEFAULT = Options(boxCount = 3, isTimerEnabled = false)
    }
}