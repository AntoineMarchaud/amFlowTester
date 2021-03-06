package com.amarchaud.amflowtester.utils


import com.amarchaud.amflowtester.model.flow.sub.ErrorFlow
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): ErrorFlow? {
        val converter = retrofit.responseBodyConverter<ErrorFlow>(ErrorFlow::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            ErrorFlow()
        }
    }
}