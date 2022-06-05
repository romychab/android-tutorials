package ua.cn.stu.remotemediator.ui

import ua.cn.stu.remotemediator.domain.Launch

/**
 * Represents data from [Launch] + selection state.
 */
data class LaunchUiEntity(
    override val id: Long,
    override val name: String,
    override val description: String,
    override val imageUrl: String,
    override val year: Int,
    override val launchTimestamp: Long,
    override val isSuccess: Boolean,
    val isChecked: Boolean
) : Launch {

    constructor(launch: Launch, isChecked: Boolean) : this(
        id = launch.id,
        name = launch.name,
        description = launch.description,
        imageUrl = launch.imageUrl,
        year = launch.year,
        launchTimestamp = launch.launchTimestamp,
        isSuccess = launch.isSuccess,
        isChecked = isChecked
    )

}