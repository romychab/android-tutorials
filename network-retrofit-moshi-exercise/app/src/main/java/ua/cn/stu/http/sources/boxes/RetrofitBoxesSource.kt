package ua.cn.stu.http.sources.boxes

import kotlinx.coroutines.delay
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.sources.base.BaseRetrofitSource
import ua.cn.stu.http.sources.base.RetrofitConfig
import ua.cn.stu.http.sources.boxes.entities.UpdateBoxRequestEntity

// todo #8: implement BoxesSource methods:
//          - setIsActive() -> should call 'PUT '/boxes/{boxId}'
//          - getBoxes() -> should call 'GET /boxes[?active=true|false]'
//                          and return the list of BoxAndSettings entities
class RetrofitBoxesSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), BoxesSource {

    override suspend fun setIsActive(
        boxId: Long,
        isActive: Boolean
    ) {
        TODO()
    }

    override suspend fun getBoxes(
        boxesFilter: BoxesFilter
    ): List<BoxAndSettings> = wrapRetrofitExceptions {
        delay(500)
        TODO()
    }

}