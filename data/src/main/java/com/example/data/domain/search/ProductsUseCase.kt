package com.example.data.domain.search

import javax.inject.Inject

class ProductsUseCase @Inject constructor(private val productsRepository: ProductsRepository) {

    fun searchProducts(
        searchValue: String
    ) = productsRepository.searchProducts(searchValue)

}