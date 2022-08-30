package com.paypay.android.task.data.repo

import android.util.Log
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.data.response.RatesResponse
import com.paypay.android.task.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CurrencyRepository @Inject constructor(private val apiService: ApiService) {

    private val TAG = "Currency Repository"

    private var timeStamp :Long = 0

    fun fetchCurrencyList(): Flow<List<CurrencyModel>> {
        return flow {
            val response = apiService.fetchCurrencyList( )
            val rates = getRates()
           val currencies = generateCurrencies(response, rates)

            Log.i(TAG, "fetchCurrencyList response $response")

            emit(currencies)
        }
    }


    private suspend fun getRates(): RatesResponse {
        val response = apiService.fetchRatesList()
        timeStamp = response.timestamp
        return  response
    }

    fun getTimeStamp(): Long {
        return timeStamp
    }

    private fun generateCurrencies(
        mappings: HashMap<String, String>,
        response: RatesResponse
    ): ArrayList<CurrencyModel> {
        val rates: HashMap<String, Double> = response.rates!!
        val currencies: ArrayList<CurrencyModel> = ArrayList()
        for (key in rates.keys) {

            val rate = rates[key]?.toDouble() ?: 0.0
            val name = mappings[key] ?: ""

            val c = CurrencyModel(key, name, rate)
            currencies.add(c)
        }

        return currencies
    }

}