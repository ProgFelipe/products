package com.example.search.presentation.modules.home.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Product
import com.example.data.domain.search.entities.Products
import com.example.search.BuildConfig
import com.example.search.presentation.modules.home.home.view.HomeFragmentDirections
import com.example.search.presentation.utils.BaseViewModel
import com.example.search.presentation.utils.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : BaseViewModel() {

    private lateinit var userInputValue: String

    private val _searchValueLiveData = MutableLiveData<String>()
    val searchValueLiveData get() = _searchValueLiveData

    private val _productsLiveData = MutableLiveData<Products>()
    val productsLiveData get() = _productsLiveData

    private val _navigationEventLiveData = SingleLiveData<NavDirections>()
    val navigationEventLiveData get() = _navigationEventLiveData

    fun setupView() {
        if (::userInputValue.isInitialized && userInputValue.isNotBlank()) {
            _searchValueLiveData.value = userInputValue
        }
    }

    fun searchProducts(userInputText: String) {
        if (shouldSearch(userInputText)) {
            userInputValue = userInputText
            productsUseCase.searchProducts(userInputText)
                .execute({ products: Products -> onSuccess(products) }, { error: Throwable ->
                    // Handle error
                    if (BuildConfig.DEBUG) {
                        Log.e("Error", error.message ?: "")
                    }
                })
        }
    }

    private fun shouldSearch(userInputText: String): Boolean {
        return if (::userInputValue.isInitialized.not()) {
            true
        } else {
            userInputValue != userInputText
        }
    }

    fun navigateToProductDetail(selectedProduct: Product) {
        _navigationEventLiveData.value =
            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(selectedProduct)
    }

    private fun onSuccess(products: Products) {
        _productsLiveData.value = products
    }
}