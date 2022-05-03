package ua.cn.stu.http.sources.base

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import retrofit2.HttpException
import retrofit2.Retrofit
import ua.cn.stu.http.app.model.AppException
import ua.cn.stu.http.app.model.BackendException
import ua.cn.stu.http.app.model.ConnectionException
import ua.cn.stu.http.app.model.ParseBackendResponseException
import java.io.IOException

/**
 * Base class for all OkHttp sources.
 */
open class BaseRetrofitSource(
    retrofitConfig: RetrofitConfig
) {

    val retrofit: Retrofit = retrofitConfig.retrofit

    private val moshi: Moshi = retrofitConfig.moshi
    private val errorAdapter = moshi.adapter(ErrorResponseBody::class.java)

    /**
     * Map network and parse exceptions into in-app exceptions.
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @throws ConnectionException
     */
    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: AppException) {
            throw e
        // moshi
        } catch (e: JsonDataException) {
            throw ParseBackendResponseException(e)
        } catch (e: JsonEncodingException) {
            throw ParseBackendResponseException(e)
        // retrofit
        } catch (e: HttpException) {
            throw createBackendException(e)
        // mostly retrofit
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val errorBody: ErrorResponseBody = errorAdapter.fromJson(
                e.response()!!.errorBody()!!.string()
            )!!
            BackendException(e.code(), errorBody.error)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    class ErrorResponseBody(
        val error: String
    )

}