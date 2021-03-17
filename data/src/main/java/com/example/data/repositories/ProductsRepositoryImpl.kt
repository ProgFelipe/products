package com.example.data.repositories

import com.example.data.domain.search.ProductsRepository
import com.example.data.domain.search.entities.Product
import com.example.data.network.ProductApi
import io.reactivex.Single
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductsRepository {

    override fun searchProducts(
        searchValue: String,
        apiVersion: Int
    ): Single<List<Product>> {
        return productApi.searchProducts(searchValue, apiVersion).map { it.mapToDomain() }
    }

    override fun searchSuggestions(
        searchValue: String,
        limit: Int,
        apiVersion: Int
    ): Single<List<String>> {
        return productApi.searchSuggestions(searchValue, limit, apiVersion).map { it.mapToDomain() }
    }
}