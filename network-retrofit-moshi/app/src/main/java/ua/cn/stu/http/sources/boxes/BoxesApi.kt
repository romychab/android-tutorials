package ua.cn.stu.http.sources.boxes

import retrofit2.http.*
import ua.cn.stu.http.sources.boxes.entities.GetBoxResponseEntity
import ua.cn.stu.http.sources.boxes.entities.UpdateBoxRequestEntity

interface BoxesApi {

    @GET("boxes")
    suspend fun getBoxes(@Query("active") isActive: Boolean?): List<GetBoxResponseEntity>

    @PUT("boxes/{boxId}")
    suspend fun setIsActive(
        @Path("boxId") boxId: Long,
        @Body body: UpdateBoxRequestEntity
    )

}