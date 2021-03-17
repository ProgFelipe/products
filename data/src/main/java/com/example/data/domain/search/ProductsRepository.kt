package com.example.data.domain.search

import com.example.data.domain.search.entities.Product
import io.reactivex.Single

interface ProductsRepository {
    companion object {
        private const val DEFAULT_QUERY_LIMIT = 6
        private const val API_VERSION = 2
    }

    fun searchProducts(
        searchValue: String,
        apiVersion: Int = API_VERSION
    ): Single<List<Product>>

    fun searchSuggestions(
        searchValue: String, limit: Int = DEFAULT_QUERY_LIMIT,
        apiVersion: Int = API_VERSION
    ): Single<List<String>>
}