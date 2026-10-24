package com.laioffer.spotify

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.platform.ComposeView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.laioffer.spotify.player.PlayerBar
import com.laioffer.spotify.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // The layout doesn't declare app:navGraph, so the graph must be set on every
        // onCreate; the NavController then restores its saved back stack on top of it.
        navController.setGraph(R.navigation.nav_graph)
        NavigationUI.setupWithNavController(navView, navController)
        navView.setOnItemSelectedListener {
            NavigationUI.onNavDestinationSelected(it, navController)
            navController.popBackStack(it.itemId, inclusive = false)
            true
        }

        findViewById<ComposeView>(R.id.player_bar).setContent {
            MaterialTheme(colors = darkColors()) {
                PlayerBar(playerViewModel)
            }
        }
    }
}
