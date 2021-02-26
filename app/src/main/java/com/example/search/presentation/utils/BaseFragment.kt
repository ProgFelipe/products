package com.example.search.presentation.utils

import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.search.R
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_LONG

open class BaseFragment : Fragment() {

    fun ServiceStatus.handleStatus(view: ConstraintLayout, progressBar: ProgressBar) {
        when (this) {
            ServiceStatus.PENDING -> progressBar.isVisible = true
            ServiceStatus.NO_INTERNET -> {
                progressBar.isVisible = false
                displayNoInternetMessage(view)
            }
            ServiceStatus.ERROR -> {
                progressBar.isVisible = false
                displayErrorMessage(view)
            }
            else -> progressBar.isVisible = false
        }
    }

    private fun displayNoInternetMessage(view: ConstraintLayout) {
        Snackbar.make(view, getString(R.string.no_internet_message), LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor(
                    context,
                    R.color.orange
                )
            )
            show()
        }
    }

    private fun displayErrorMessage(view: ConstraintLayout) {
        Snackbar.make(view, getString(R.string.generic_error_message), LENGTH_LONG).apply {
            setBackgroundTint(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )
            show()
        }
    }
}