package com.example.search.presentation.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.setupImageUri(context: Context, imageUri: String?) {
    imageUri?.let {
        Glide.with(context)
            .load(it)
            .fitCenter()
            .into(this)
    }
}