package com.paypay.android.task.data.helper

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.network.Constants.PREFERENCE_NAME
import java.util.*

class ComplexPreferencesImpl(context: Context) {
    var sharedPreferences: SharedPreferences
    private val gson: Gson = Gson()
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    fun saveArrayList(list: ArrayList<CurrencyModel>, key: String) {
        val json: String = gson.toJson(list)
        editor.putString(key, json)
        editor.apply()
    }

    fun saveTimeStamp(time: Long , key: String) {
        val json: String = gson.toJson(time)
        editor.putString(key, json)
        editor.apply()
    }

    fun getTimeStamp(key: String) : Long?{
        val json: String? = sharedPreferences.getString(key, null)
        val type = object : TypeToken<Long>(){}.type
        return gson.fromJson(json, type)
    }

    fun getArrayList(key: String): ArrayList<CurrencyModel>? {
        val json: String? = sharedPreferences.getString(key, null)
        val type = object : TypeToken<ArrayList<CurrencyModel>>() {}.type
        return gson.fromJson(json, type)
    }
}