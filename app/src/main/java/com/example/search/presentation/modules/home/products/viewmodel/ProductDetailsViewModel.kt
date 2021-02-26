package com.example.search.presentation.modules.home.products.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.data.domain.search.ProductsUseCase
import com.example.data.domain.search.entities.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor() : ViewModel() {

    private lateinit var product: Product

    private val _productLiveData = MutableLiveData<Product>()
    val productLiveData get() = _productLiveData

    fun setupArgs(product: Product) {
        this.product = product
    }

    fun setupView(){
        _productLiveData.value = product
    }
}