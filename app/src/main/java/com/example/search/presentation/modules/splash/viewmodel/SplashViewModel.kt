package com.example.search.presentation.modules.splash.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.search.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel() {

    private val _navigateToHome: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val navigateToHome get() = _navigateToHome

    fun initialize() {
        navigateToHome()
    }

    private fun navigateToHome() {
        _navigateToHome.value = true
    }
}