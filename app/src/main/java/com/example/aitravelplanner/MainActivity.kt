package com.example.aitravelplanner

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.aitravelplanner.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarItemView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //retrieving the central icon of the bottom navigation and setting the size differently
        val navView: BottomNavigationView = binding.navView
        val menu = navView.menu
        val menuItem = menu.getItem(1)
        val navigationBarItemView: NavigationBarItemView = navView.findViewById(menuItem.itemId)
        val centralIconView: View = navigationBarItemView.findViewById(com.google.android.material.R.id.navigation_bar_item_icon_view)
        val centralIconViewParams: FrameLayout.LayoutParams = centralIconView.layoutParams as FrameLayout.LayoutParams
        centralIconViewParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65F, resources.displayMetrics).toInt()
        centralIconViewParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65F, resources.displayMetrics).toInt()
        centralIconViewParams.bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,4F, resources.displayMetrics).toInt()
        centralIconViewParams.topMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2F, resources.displayMetrics).toInt()
        centralIconView.layoutParams = centralIconViewParams


        // setting the nav controller for the bottom vavigation view
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
    }
}