package com.example.stalk

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.stalk.databinding.ActivityMainBinding
import android.util.Log
import androidx.activity.viewModels
import com.example.stalk.ui.viewmodel.TradeViewModel
import androidx.lifecycle.Observer
import com.example.stalk.ui.viewmodel.NameViewModel

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
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_saved))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        tradeViewModel.trades.observe(this, Observer { trades ->
            // Update the UI with the list of trades
            trades?.let {
                for (trade in it) {
                    Log.d("MainActivity", trade.toString())
                }
            }
        })

        nameViewModel.names.observe(this, Observer { names ->
            // Update the UI with the list of trades
            names?.let {
                for (name in it) {
                    Log.d("MainActivity", name.toString())
                }
            }
        })

        nameViewModel.fetchNames()

        // Fetch trades (example)
        tradeViewModel.fetchRecentTrades(10)
    }
}