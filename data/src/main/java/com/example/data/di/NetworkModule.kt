package com.example.data.di

import com.example.data.BuildConfig
import com.example.data.network.ProductApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun providesOkHttpClient() = OkHttpClient
        .Builder()
        .build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(
            RxJava2CallAdapterFactory.createWithScheduler(
                Schedulers.io()
            )
        )
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun providesProductApi(retrofit: Retrofit): ProductApi = retrofit.create(ProductApi::class.java)
}