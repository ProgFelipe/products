package com.example.search.presentation.di

import android.content.Context
import com.example.search.presentation.utils.NetworkUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ApplicationModule {
    @Provides
    @ViewModelScoped
    fun providesNetworkUtils(@ApplicationContext applicationContext: Context) =
        NetworkUtils(applicationContext)
}