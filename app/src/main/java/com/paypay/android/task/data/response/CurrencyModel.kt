package com.paypay.android.task.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class CurrencyModel(val id: String, val name : String, var rate : Double = 0.0  ): Parcelable
