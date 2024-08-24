package com.wtw.whattheweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.wtw.whattheweather.databinding.ActivityWeatherBinding

class WeatherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_weather) as NavHostFragment
        val navController = navHostFragment.navController

        val navView: BottomNavigationView = binding.navView

        navView.setupWithNavController(navController)
    }
}