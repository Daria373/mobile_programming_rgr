package com.example.rgr.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.rgr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Налаштовуємо Toolbar
        setSupportActionBar(binding.toolbar)

        // Отримуємо NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(com.example.rgr.R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Верхня панель (Toolbar) — автоматично змінює назву
        setupActionBarWithNavController(navController)

        // Нижня навігація
        binding.bottomNavigation.setupWithNavController(navController)
    }

    // Підтримка кнопки "Назад" у Toolbar
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(com.example.rgr.R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}