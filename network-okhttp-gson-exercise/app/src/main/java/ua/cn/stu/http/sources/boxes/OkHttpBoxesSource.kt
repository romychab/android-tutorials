package ua.cn.stu.http.sources.boxes

import kotlinx.coroutines.delay
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.sources.base.BaseOkHttpSource
import ua.cn.stu.http.sources.base.OkHttpConfig

// todo #7: implement methods:
//          - setIsActive() -> for making box active or inactive
//          - getBoxes() -> for fetching boxes data
class OkHttpBoxesSource(
    config: OkHttpConfig
) : BaseOkHttpSource(config), BoxesSource {

    override suspend fun setIsActive(boxId: Long, isActive: Boolean) {
        // Call "PUT /boxes/{boxId}" endpoint.
        // Use UpdateBoxRequestEntity.
        TODO()
    }

    override suspend fun getBoxes(boxesFilter: BoxesFilter): List<BoxAndSettings> {
        delay(500)
        // Call "GET /boxes?active=true" if boxesFilter = ONLY_ACTIVE.
        // Call "GET /boxes" if boxesFilter = ALL.
        // Hint: use TypeToken for converting server response into List<GetBoxResponseEntity>
        // Hint: use GetBoxResponseEntity.toBoxAndSettings for mapping GetBoxResponseEntity into BoxAndSettings
        TODO()
    }

}