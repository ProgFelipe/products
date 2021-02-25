package com.example.data.domain.search.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val title: String?,
    val price: Long?,
    val thumbnailUri: String?,
    val cityLocation: String?
) : Parcelable
