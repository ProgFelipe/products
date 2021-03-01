package com.example.search.presentation.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.search.R

fun ImageView.setupImageUri(context: Context, imageUri: String?) {
    imageUri?.let {
        Glide.with(context)
            .load(it)
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_error)
            .fitCenter()
            .into(this)
    }
}