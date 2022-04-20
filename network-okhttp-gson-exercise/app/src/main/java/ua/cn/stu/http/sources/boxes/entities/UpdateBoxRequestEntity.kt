package ua.cn.stu.http.sources.boxes.entities

/**
 * Request body for `PUT /boxes/{id}` HTTP-request for activating/deactivating
 * the specified box.
 *
 * JSON example:
 * ```
 * {
 *   "isActive": true
 * }
 * ```
 */
data class UpdateBoxRequestEntity(
    val isActive: Boolean
)