package ua.cn.stu.navcomponent.tabs.model.boxes

import kotlinx.coroutines.flow.Flow
import ua.cn.stu.navcomponent.tabs.model.boxes.entities.Box

interface BoxesRepository {

    /**
     * Get the list of boxes.
     * @param onlyActive if set to `true` then only active boxes are emitted.
     */
    fun getBoxes(onlyActive: Boolean = false): Flow<List<Box>>

    /**
     * Mark the specified box as active. Only active boxes are displayed in dashboard screen.
     */
    suspend fun activateBox(box: Box)

    /**
     * Mark the specified box as inactive. Inactive boxes are not displayed in dashboard screen.
     */
    suspend fun deactivateBox(box: Box)

}