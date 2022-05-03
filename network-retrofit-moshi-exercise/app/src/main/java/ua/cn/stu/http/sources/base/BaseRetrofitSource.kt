package ua.cn.stu.http.sources.base

import ua.cn.stu.http.app.model.BackendException
import ua.cn.stu.http.app.model.ConnectionException
import ua.cn.stu.http.app.model.ParseBackendResponseException

// todo #4: add property for accessing Retrofit instance and implement
//          wrapRetrofitExceptions() method.
open class BaseRetrofitSource(
    retrofitConfig: RetrofitConfig
) {

    /**
     * Map network and parse exceptions into in-app exceptions.
     * @throws BackendException
     * @throws ParseBackendResponseException
     * @throws ConnectionException
     */
    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        // execute 'block' passed to arguments and throw:
        // - BackendException with code and message if server has returned error response
        // - ParseBackendResponseException if server response can't be parsed
        // - ConnectionException if HTTP request has failed
        TODO()
    }

}