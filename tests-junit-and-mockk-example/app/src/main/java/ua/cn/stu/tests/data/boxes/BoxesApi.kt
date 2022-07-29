package ua.cn.stu.tests.data.boxes

import retrofit2.http.*
import ua.cn.stu.tests.data.boxes.entities.GetBoxResponseEntity
import ua.cn.stu.tests.data.boxes.entities.UpdateBoxRequestEntity

interface BoxesApi {

    @GET("boxes")
    suspend fun getBoxes(@Query("active") isActive: Boolean?): List<GetBoxResponseEntity>

    @PUT("boxes/{boxId}")
    suspend fun setIsActive(
        @Path("boxId") boxId: Long,
        @Body body: UpdateBoxRequestEntity
    )

}