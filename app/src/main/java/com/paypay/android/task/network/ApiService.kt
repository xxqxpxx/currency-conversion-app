package com.paypay.android.task.network

import com.paypay.android.task.data.response.RatesResponse
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.CURRENCY_LIST_URL)
    suspend fun fetchCurrencyList(): HashMap<String, String>


    @GET(Constants.RATES_LIST)
    suspend fun fetchRatesList(): RatesResponse



}