package com.example.search.presentation.modules.home.home.viewmodel

import com.example.data.domain.search.ProductsUseCase
import com.example.search.presentation.utils.BaseViewModel
import com.example.search.presentation.utils.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productsUseCase: ProductsUseCase
) : BaseViewModel() {

}