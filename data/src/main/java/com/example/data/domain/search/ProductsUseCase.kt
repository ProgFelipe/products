package com.example.data.domain.search

import javax.inject.Inject

class ProductsUseCase @Inject constructor(
    val productsRepository: ProductsRepository
) {
}