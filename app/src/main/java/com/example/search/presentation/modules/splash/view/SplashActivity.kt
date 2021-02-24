package com.example.search.presentation.modules.splash.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.search.R
import com.example.search.presentation.modules.home.HomeActivity
import com.example.search.presentation.modules.splash.viewmodel.SplashViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        initializeListeners()
        viewModel.initialize()
    }

    private fun initializeListeners() {
        viewModel.navigateToHome.observe(this, {
            startActivity(HomeActivity.getIntent(this@SplashActivity))
        })
    }
}