package ua.cn.stu.http.sources.base

import com.google.gson.Gson
import okhttp3.OkHttpClient

/**
 * All stuffs required for making HTTP-requests with OkHttp client and
 * for parsing JSON-messages.
 */
class OkHttpConfig(
    val baseUrl: String,        // prefix for all endpoints
    val client: OkHttpClient,   // for making HTTP requests
    val gson: Gson              // for parsing JSON-messages
)