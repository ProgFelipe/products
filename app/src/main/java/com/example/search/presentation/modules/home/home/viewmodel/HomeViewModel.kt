package com.example.search.presentation.modules.home.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Product
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

    companion object {
        private const val ERROR = "Error"
        private const val EMPTY_STRING = ""
        private const val MINIMUM_SUGGESTION_LENGTH = 3
    }

    private var _userInputValue: String = EMPTY_STRING

    // Encapsulated field
    var userInputValue: String
        get() = _userInputValue
        set(value) {
            _userInputValue = value
        }

    private val _suggestedProductsLiveData = MutableLiveData<List<String>>()
    val suggestedProductsLiveData get() = _suggestedProductsLiveData

    private val _productsLiveData = MutableLiveData<List<Product>>()
    val productsLiveData get() = _productsLiveData

    private val _navigationEventLiveData = SingleLiveData<NavDirections>()
    val navigationEventLiveData get() = _navigationEventLiveData

    fun searchProducts(userInputText: String) {
        _userInputValue = userInputText
        searchProducts()
    }

    private fun searchProducts() {
        productsUseCase.searchProducts(_userInputValue)
            .execute({ products: List<Product> -> onSuccess(products) }, { error: Throwable ->
                // Handle error
                if (BuildConfig.DEBUG) {
                    Log.e(ERROR, error.message ?: EMPTY_STRING)
                }
            })
    }

    private fun userInputChanged(userInputText: String): Boolean {
        return _userInputValue != userInputText
    }

    fun navigateToProductDetail(selectedProduct: Product) {
        _navigationEventLiveData.value =
            HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(selectedProduct)
    }

    private fun onSuccess(products: List<Product>?) {
        _productsLiveData.value = products
    }

    fun searchSuggestions(userInputText: String) {
        _userInputValue = userInputText
        productsUseCase.searchSuggestions(_userInputValue)
            .execute(
                { suggestedQueries: List<String> -> onSearchSuggestionSuccess(suggestedQueries) },
                { error: Throwable ->
                    // Handle error
                    if (BuildConfig.DEBUG) {
                        Log.e(ERROR, error.message ?: EMPTY_STRING)
                    }
                })
    }

    fun shouldSuggestFilter(userInputText: String): Boolean {
        return userInputText.isNotEmpty() && userInputChanged(userInputText) && userInputText.length >= MINIMUM_SUGGESTION_LENGTH
    }

    private fun onSearchSuggestionSuccess(suggestedQueries: List<String>) {
        _suggestedProductsLiveData.value = suggestedQueries
    }
}