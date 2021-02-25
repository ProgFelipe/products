package com.example.search.presentation.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.search.presentation.utils.ServiceStatus.*
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var networkUtils: NetworkUtils
    private val compositeDisposable = CompositeDisposable()

    private val observeOnScheduler by lazy { AndroidSchedulers.mainThread() }
    private val subscribeOnScheduler by lazy { Schedulers.single() }

    private val _serviceStatus = MutableLiveData<ServiceStatus>()
    val serviceStatusLiveData get() = _serviceStatus

    fun <T> Single<T>.execute(success: (response: T) -> Unit, error: (error: Throwable) -> Unit) {
        _serviceStatus.value = PENDING
        if (networkUtils.isNetworkAvailable()) {
            compositeDisposable.add(
                this.subscribeOn(subscribeOnScheduler)
                    .observeOn(observeOnScheduler).doOnSuccess {
                        success(it)
                        _serviceStatus.value = SUCCESS
                    }.doOnError {
                        _serviceStatus.value = ERROR
                        error(it)
                    }.subscribe()
            )
        } else {
            _serviceStatus.value = NO_INTERNET
        }
    }

    fun unsubscribe() {
        compositeDisposable.clear()
    }
}