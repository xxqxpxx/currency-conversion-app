package com.paypay.android.task.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class RatesResponse (
    val timestamp: Long = 0,
    val rates: HashMap<String, Double>? = null
) : Parcelable

