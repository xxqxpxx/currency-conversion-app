package com.paypay.android.task.network

import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

object ErrorManager {

    fun getCode(throwable: Throwable?): Int {
        return when (throwable) {
            is IOException -> {
                0
            }
            is SocketTimeoutException -> {
                0
            }
            is HttpException -> {
                throwable.code()
            }
            else -> {
                0
            }
        }
    }
}