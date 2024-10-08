package com.example.stalk

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted, proceed with setup
                setupNavigation()
            } else {
                // Permission is denied, show a message and exit
                Toast.makeText(this, "Notification permission is required for this app, please enable it in settings.", Toast.LENGTH_LONG).show()
                finish() // Exit the app
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            showEducationalDialog()
        } else {
            setupNavigation()
        }
    }

    private fun showEducationalDialog() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer")
            .setMessage("This app is for educational purposes only. Use the information at your own risk and ensure you comply with all trading regulations. We are not responsible for any financial losses.")
            .setPositiveButton("Agree") { _, _ ->
                requestNotificationPermission()
            }
            .setNegativeButton("Disagree") { _, _ ->
                Toast.makeText(this, "You must agree to continue using the app.", Toast.LENGTH_LONG).show()
                finish() // Exit the app
            }
            .show()
    }

    private fun requestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            // Permission already granted
            setupNavigation()
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_search, R.id.navigation_saved)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

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
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
