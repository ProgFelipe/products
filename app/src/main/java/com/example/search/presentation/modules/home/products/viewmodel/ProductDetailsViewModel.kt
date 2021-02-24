package com.example.search.presentation.modules.home.products.viewmodel

import androidx.lifecycle.ViewModel
import com.example.data.domain.search.ProductsUseCase
import com.example.search.presentation.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : ViewModel() {

}