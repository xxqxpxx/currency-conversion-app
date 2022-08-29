package com.paypay.android.task.network

import com.paypay.android.task.data.response.RatesResponse
import com.paypay.android.task.data.response.SearchCityResoponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {


    @GET(Constants.CURRENCY_LIST_URL)
    suspend fun fetchCurrencyList(): HashMap<String, String>


    @GET(Constants.RATES_LIST)
    suspend fun fetchRatesList(): RatesResponse



    @GET(Constants.CITY_SEARCH_URL)
    suspend fun searchForCity(
        @Query("q") city: String? = "Dubai",
    ): SearchCityResoponse


}