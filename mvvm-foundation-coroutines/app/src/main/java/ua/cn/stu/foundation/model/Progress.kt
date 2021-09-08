package ua.cn.stu.foundation.model

/**
 * Represents the progress state of some operation: whether progress should be displayed or not.
 */
sealed class Progress

/**
 * Progress should not be displayed
 */
object EmptyProgress : Progress()

/**
 * Progress should be displayed and also may indicate the percentage value.
 */
data class PercentageProgress(
    val percentage: Int
) : Progress() {

    companion object {
        val START = PercentageProgress(percentage = 0)
    }

}

// --- extension methods

/**
 * @return whether operation is in progress or not
 */
fun Progress.isInProgress() = this !is EmptyProgress

/**
 * @return percentage of operation if possible; otherwise [PercentageProgress.START].
 */
fun Progress.getPercentage() = (this as? PercentageProgress)?.percentage ?: PercentageProgress.START.percentage