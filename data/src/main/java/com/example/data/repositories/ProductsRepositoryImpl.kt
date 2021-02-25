package com.example.data.repositories

import com.example.data.domain.search.ProductsRepository
import com.example.data.domain.search.entities.Products
import com.example.data.network.ProductApi
import io.reactivex.Single
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val productApi: ProductApi
) : ProductsRepository {

    override fun searchProducts(
        searchValue: String,
        limit: Int,
        apiVersion: Int
    ): Single<Products> {
        return productApi.searchProducts(searchValue, limit, apiVersion).map { it.mapToDomain() }
    }
}