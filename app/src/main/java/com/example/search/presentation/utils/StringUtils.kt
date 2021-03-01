package com.example.search.presentation.utils

import com.example.search.BuildConfig
import java.text.NumberFormat
import java.util.*

private const val DEFAULT_CURRENCY = "COP"

fun Long.toMoneyFormat(): String {
    return try {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.maximumFractionDigits = 0
        format.currency = Currency.getInstance(DEFAULT_CURRENCY)

        val result = format.format(this)
        result
    } catch (e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
        this.toString()
    }

}