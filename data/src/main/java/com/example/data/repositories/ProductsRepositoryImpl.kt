package com.example.data.repositories

import com.example.data.domain.search.ProductsRepository
import com.example.data.network.ProductApi
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductsRepository {
}