package ua.cn.stu.http.sources.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ua.cn.stu.http.app.model.AppException
import ua.cn.stu.http.app.model.BackendException
import ua.cn.stu.http.app.model.ConnectionException
import ua.cn.stu.http.app.model.ParseBackendResponseException
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Base class for all OkHttp sources.
 */
open class BaseOkHttpSource(
    private val config: OkHttpConfig
) {
    val gson: Gson = config.gson
    val client: OkHttpClient = config.client

    private val contentType = "application/json; charset=utf-8".toMediaType()

    /**
     * Suspending function which wraps OkHttp [Call.enqueue] method for making
     * HTTP requests and wraps external exceptions into subclasses of [AppException].
     *
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    val appException = ConnectionException(e)
                    continuation.resumeWithException(appException)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        // well done
                        continuation.resume(response)
                    } else {
                        handleErrorResponse(response, continuation)
                    }
                }
            })
        }
    }

    /**
     * Concatenate the base URL with a path and query args.
     */
    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        url("${config.baseUrl}$endpoint")
        return this
    }

    /**
     * Convert data class into [RequestBody] in JSON-format.
     */
    fun <T> T.toJsonRequestBody(): RequestBody {
        val json = gson.toJson(this)
        return json.toRequestBody(contentType)
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * TypeToken passed to this function as a second argument. Usually this method is
     * used to parse JSON arrays.
     *
     * @throws ParseBackendResponseException
     */
    fun <T> Response.parseJsonResponse(typeToken: TypeToken<T>): T {
        try {
            return gson.fromJson(this.body!!.string(), typeToken.type)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * the generic type [T]. Usually this method is used to parse JSON objects.
     *
     * @throws ParseBackendResponseException
     */
    inline fun <reified T> Response.parseJsonResponse(): T {
        try {
            return gson.fromJson(this.body!!.string(), T::class.java)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * 1. Convert error response from the server into [BackendException] and throw the latter.
     * 2. Throw [ParseBackendResponseException] if error response parsing
     * process has been failed.
     */
    private fun handleErrorResponse(response: Response,
                                    continuation: CancellableContinuation<Response>) {
        val httpCode = response.code
        try {
            // parse error body:
            // {
            //   "error": "..."
            // }
            val map = gson.fromJson(response.body!!.string(), Map::class.java)
            val message = map["error"].toString()
            continuation.resumeWithException(BackendException(httpCode, message))
        } catch (e: Exception) {
            // failed to parse error body -> throw parse exception
            val appException = ParseBackendResponseException(e)
            continuation.resumeWithException(appException)
        }
    }

}