package ua.cn.stu.hilt.app.model.boxes

import ua.cn.stu.hilt.app.model.BackendException
import ua.cn.stu.hilt.app.model.ConnectionException
import ua.cn.stu.hilt.app.model.ParseBackendResponseException
import ua.cn.stu.hilt.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.hilt.app.model.boxes.entities.BoxesFilter

interface BoxesSource {

    /**
     * Get the list of all boxes for the current logged-in user.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings>

    /**
     * Set isActive flag for the specified box.
     * @throws BackendException
     * @throws ConnectionException
     * @throws ParseBackendResponseException
     */
    suspend fun setIsActive(boxId: Long, isActive: Boolean)

}
