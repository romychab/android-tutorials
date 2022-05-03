package ua.cn.stu.http.sources.boxes

import kotlinx.coroutines.delay
import ua.cn.stu.http.app.model.boxes.BoxesSource
import ua.cn.stu.http.app.model.boxes.entities.BoxAndSettings
import ua.cn.stu.http.app.model.boxes.entities.BoxesFilter
import ua.cn.stu.http.sources.base.BaseRetrofitSource
import ua.cn.stu.http.sources.base.RetrofitConfig
import ua.cn.stu.http.sources.boxes.entities.UpdateBoxRequestEntity

class RetrofitBoxesSource(
    config: RetrofitConfig
) : BaseRetrofitSource(config), BoxesSource {

    private val boxesApi = retrofit.create(BoxesApi::class.java)

    override suspend fun getBoxes(
        boxesFilter: BoxesFilter
    ): List<BoxAndSettings> = wrapRetrofitExceptions {
        delay(500)
        val isActive: Boolean? = if (boxesFilter == BoxesFilter.ONLY_ACTIVE)
            true
        else
            null

        boxesApi.getBoxes(isActive).map { it.toBoxAndSettings() }
    }

    override suspend fun setIsActive(
        boxId: Long,
        isActive: Boolean
    ) = wrapRetrofitExceptions {
        val updateBoxRequestEntity = UpdateBoxRequestEntity(isActive)
        boxesApi.setIsActive(boxId, updateBoxRequestEntity)
    }

}