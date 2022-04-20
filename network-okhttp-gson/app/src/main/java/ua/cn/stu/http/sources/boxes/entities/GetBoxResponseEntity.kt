package ua.cn.stu.http.sources.boxes.entities

import android.graphics.Color
import ua.cn.stu.http.app.model.boxes.entities.Box
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings

/**
 * Response body for `GET /boxes` and `GET /box/{id}` HTTP-requests.
 *
 * JSON examples:
 * - `GET /boxes` (array)
 *   ```
 *   [
 *     {
 *       "id": 0,
 *       "colorName": "",
 *       "colorValue": "#000000",
 *       "isActive": true
 *     },
 *     ...
 *   ]
 *   ```
 * - `GET /boxes/{id}` (one entity):
 *   ```
 *   {
 *     "id": 0,
 *     "colorName": "",
 *     "colorValue": "#000000",
 *     "isActive": true
 *   }
 *   ```
 */

data class GetBoxResponseEntity(
    val id: Long,
    val colorName: String,
    val colorValue: String,
    val isActive: Boolean
) {

    fun toBoxAndSettings(): BoxAndSettings = BoxAndSettings(
        Box(
            id = id,
            colorName = colorName,
            colorValue = Color.parseColor(colorValue)
        ),
        isActive = isActive
    )

}
