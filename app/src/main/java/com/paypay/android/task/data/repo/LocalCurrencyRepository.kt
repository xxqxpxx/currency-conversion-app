package com.paypay.android.task.data.repo

import com.paypay.android.task.data.helper.ComplexPreferencesImpl
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.network.Constants.RATES_LIST_PREF_NAME
import com.paypay.android.task.network.Constants.TIME_STAMP_PREF_NAME


class LocalCurrencyRepository(private val complexPreferences: ComplexPreferencesImpl) {
    private val TAG = "LocalFavouritePlanetsRepository"

    fun saveCurrencyRatesList(list: ArrayList<CurrencyModel>) {
         complexPreferences.saveArrayList(list, RATES_LIST_PREF_NAME)
    }

    fun saveTimeStamp(timestamp: Long) {
        complexPreferences.saveTimeStamp(timestamp, TIME_STAMP_PREF_NAME)
    }

    fun getTimeStamp(): Long {
        val time = complexPreferences.getTimeStamp(TIME_STAMP_PREF_NAME)
        return time ?: 0
    }

      fun getCurrencyRatesList(): ArrayList<CurrencyModel> {
        val list = complexPreferences.getArrayList(RATES_LIST_PREF_NAME)

        return if (list.isNullOrEmpty()) {
            (arrayListOf())
        } else
            list
    }


}