package com.paypay.android.task.network

sealed class ResultModel<out T> {
    data class Success<out R>(val data: R) : ResultModel<R>()
    data class Failure(val code: Int) : ResultModel<Nothing>()
    data class Loading(val isLoading: Boolean? = false) : ResultModel<Nothing>()
}