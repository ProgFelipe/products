package com.example.search.presentation.modules.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.search.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    companion object {
        fun getIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val navController = findNavController(R.id.nav_host_fragment_home)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph_home)
        navGraph.startDestination = R.id.homeFragment
        navController.graph = navGraph
    }
}