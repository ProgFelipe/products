package com.example.search.presentation.utils

import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.search.R

open class BaseFragment : Fragment() {

    fun ServiceStatus.handleStatus(progressBar: ProgressBar) {
        when (this) {
            ServiceStatus.PENDING -> progressBar.isVisible = true
            ServiceStatus.NO_INTERNET -> {
                progressBar.isVisible = false
                displayNoInternetMessage()
            }
            ServiceStatus.ERROR -> {
                progressBar.isVisible = false
                displayErrorMessage()
            }
            else -> progressBar.isVisible = false
        }
    }

    private fun displayNoInternetMessage() {
        Toast.makeText(
            activity, getString(R.string.no_internet_message),
            Toast.LENGTH_LONG
        ).show()
    }

    private fun displayErrorMessage() {
        Toast.makeText(
            activity, getString(R.string.generic_error_message),
            Toast.LENGTH_LONG
        ).show()
    }

}