package com.example.spoonacular.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.spoonacular.R
import com.example.spoonacular.databinding.ActivityHomeScreenBinding
import com.example.spoonacular.ui.fragments.FavouritesFragment
import com.example.spoonacular.ui.fragments.HomeFragment

class HomeScreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeScreenBinding
    private var isHomeSelected = true

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadFragment(HomeFragment(), "Home")

        updateMenuItemIcon(isHomeSelected)

        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    if (!isHomeSelected) {
                        loadFragment(HomeFragment(), "Home")
                        isHomeSelected = true
                        updateMenuItemIcon(isHomeSelected)
                    }
                }

                R.id.navigation_reels -> {
                    if (isHomeSelected) {
                        loadFragment(FavouritesFragment(), "Favourites")
                        isHomeSelected = false
                        updateMenuItemIcon(isHomeSelected)
                    }
                }
            }
            true
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updateMenuItemIcon(isHomeSelected: Boolean) {
        val menu = binding.bottomNav.menu
        for (i in 0 until menu.size()) {
            val menuItem = menu.getItem(i)
            menuItem.icon = when (menuItem.itemId) {
                R.id.navigation_home -> {
                    if (isHomeSelected) getDrawable(R.drawable.img_9) else getDrawable(R.drawable.img_7)
                }

                R.id.navigation_reels -> {
                    if (!isHomeSelected) getDrawable(R.drawable.img_10) else getDrawable(R.drawable.img_8)
                }

                else -> menuItem.icon // For other menu items, retain the existing icon
            }
        }
    }

    private fun loadFragment(fragment: Fragment, title: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(title)
            .commit()
    }
}
