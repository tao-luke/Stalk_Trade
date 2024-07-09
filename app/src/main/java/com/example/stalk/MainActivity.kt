package com.example.stalk

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.stalk.databinding.ActivityMainBinding
import com.example.stalk.ui.viewmodel.NameViewModel
import com.example.stalk.ui.viewmodel.TradeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tradeViewModel: TradeViewModel by viewModels()
    private val nameViewModel: NameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_search, R.id.navigation_saved)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        tradeViewModel.overviewTrades.observe(this, Observer { trades ->
            trades?.let {
                for (trade in it) {
                    Log.d("MainActivity", trade.toString())
                }
            }
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> navView.menu.findItem(R.id.navigation_home).isChecked = true
                R.id.navigation_search -> navView.menu.findItem(R.id.navigation_search).isChecked = true
                R.id.navigation_saved -> navView.menu.findItem(R.id.navigation_saved).isChecked = true
                R.id.politicianFragment -> {
                    val previousBackStackEntry = navController.previousBackStackEntry
                    if (previousBackStackEntry != null) {
                        when (previousBackStackEntry.destination.id) {
                            R.id.navigation_saved -> navView.menu.findItem(R.id.navigation_saved).isChecked = true
                            R.id.navigation_search -> navView.menu.findItem(R.id.navigation_search).isChecked = true
                        }
                    }
                }
            }
        }

        nameViewModel.names.observe(this, Observer { names ->
            names?.let {
                for (name in it) {
                    Log.d("MainActivity", name.toString())
                }
            }
        })

        nameViewModel.fetchNames()
        tradeViewModel.fetchRecentTrades(10)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
