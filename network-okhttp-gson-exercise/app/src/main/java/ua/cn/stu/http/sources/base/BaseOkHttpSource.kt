package ua.cn.stu.http.sources.base

import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import ua.cn.stu.http.app.model.AppException
import ua.cn.stu.http.app.model.BackendException
import ua.cn.stu.http.app.model.ConnectionException
import ua.cn.stu.http.app.model.ParseBackendResponseException

/**
 * Base class for all OkHttp sources.
 */
// todo #5: implement base source which uses OkHttp library for making HTTP requests
//          and GSON library for working with JSON data.
//          Implement the following methods:
//          - Call.suspendEnqueue() -> suspending function which should wrap OkHttp callbacks
//          - Request.Builder.endpoint() -> for concatenating Base URL and endpoint.
//          - T.toJsonRequestBody() -> for serializing any data class into JSON request body.
//          - Response.parseJsonResponse() -> for deserializing server responses into data classes.
open class BaseOkHttpSource(
    private val config: OkHttpConfig
) {

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
            // TODO
        }
    }

    /**
     * Concatenate the base URL with a path and query args.
     */
    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        TODO()
    }

    /**
     * Convert data class into [RequestBody] in JSON-format.
     */
    fun <T> T.toJsonRequestBody(): RequestBody {
        TODO("Convert T object into RequestBody object")
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * TypeToken passed to this function as a second argument. Usually this method is
     * used to parse JSON arrays.
     *
     * @throws ParseBackendResponseException
     */
    fun <T> Response.parseJsonResponse(typeToken: TypeToken<T>): T {
        TODO("Convert Response object into T object by using TypeToken")
    }

    /**
     * Parse OkHttp [Response] instance into data object. The type is derived from
     * the generic type [T]. Usually this method is used to parse JSON objects.
     *
     * @throws ParseBackendResponseException
     */
    inline fun <reified T> Response.parseJsonResponse(): T {
        TODO("Convert Response object into T object by using Class<T>")
    }

}