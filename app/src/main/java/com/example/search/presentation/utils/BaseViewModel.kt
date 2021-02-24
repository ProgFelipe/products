package com.example.search.presentation.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var networkUtils: NetworkUtils

    enum class ServiceStatus {
        READY,
        PENDING,
        SUCCESS,
        ERROR
    }

    private val status = MutableLiveData<ServiceStatus>()
    val serviceStatus get() = status

}