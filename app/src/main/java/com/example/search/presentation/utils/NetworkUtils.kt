package com.example.search.presentation.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import javax.inject.Singleton

@Singleton
class NetworkUtils constructor(
    private val context: Context
) {
    fun isNetworkAvailable(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectivityManager?.run {
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)?.let {
                    result = when {
                        it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        it.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        else -> false
                    }
                }
            }
        } else {
            connectivityManager?.run {
                connectivityManager.activeNetworkInfo?.run {
                    when (type) {
                        ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}