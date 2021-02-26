package com.example.data.network

import com.example.data.model.ProductsDto
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {

    companion object {
        private const val COLOMBIA_SITE_CODE = "MCO"
    }

    @GET("$COLOMBIA_SITE_CODE/search")
    fun searchProducts(
        @Query("q") searchValue: String,
        @Query("limit") limit: Int,
        @Query("api_version") apiVersion: Int
    ): Single<ProductsDto>
}