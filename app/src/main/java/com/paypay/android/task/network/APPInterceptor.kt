package com.paypay.android.task.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class APPInterceptor : Interceptor {

    val TAG = "APPInterceptor"

    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()

        val originalUrl = originalRequest.url

        val url = originalUrl.newBuilder().build()

        val requestBuilder = originalRequest.newBuilder().url(url)
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; utf-8")
        val request = requestBuilder.build()

        val response = chain.proceed(request)

        Log.i(TAG, "request : ${request.url}")
        Log.i(TAG, "header : ${request.headers}")
        Log.i(TAG, "response : ${response.body.toString()}")

        response.code//status code
        return response
    } // fun of intercept
} // class of APPInterceptor