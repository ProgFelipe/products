package com.example.data.di

import com.example.data.BuildConfig.BASE_URL
import com.example.data.BuildConfig.DEBUG
import com.example.data.network.ProductApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    companion object {
        private const val TIME_LIMIT = 60000L
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIME_LIMIT, MILLISECONDS)
            .writeTimeout(TIME_LIMIT, MILLISECONDS)
            .readTimeout(TIME_LIMIT, MILLISECONDS)

        // Logs if Debug
        if (DEBUG) {
            val httpLoginInterceptor = HttpLoggingInterceptor()
            httpLoginInterceptor.level = BODY
            okHttpClient.addInterceptor(httpLoginInterceptor)
        }

        return okHttpClient.build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
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