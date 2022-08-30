package com.paypay.android.task.network

import com.paypay.android.task.BuildConfig
import com.paypay.android.task.BuildConfig.API_KEY

object Constants {

    const val API_TIMEOUT: Long = 60

    const val BASE_URL = BuildConfig.BASE_URL

    const val CURRENCY_LIST_URL = "currencies.json?app_id=$API_KEY"

    const val RATES_LIST = "latest.json?app_id=$API_KEY"

    const val PREFERENCE_NAME = "pref_paypay"

    const val RATES_LIST_PREF_NAME = "rates_list"

    const val TIME_STAMP_PREF_NAME = "timestamp"




}