package com.example.data.di

import com.example.data.domain.search.ProductsRepository
import com.example.data.network.ProductApi
import com.example.data.repositories.ProductsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Provides
    @Singleton
    fun providesProductRepository(productApi: ProductApi): ProductsRepository =
        ProductsRepositoryImpl(productApi)
}