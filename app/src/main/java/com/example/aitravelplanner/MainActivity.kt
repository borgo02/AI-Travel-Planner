package com.example.aitravelplanner

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.aitravelplanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var travelCardsRecyclerView: RecyclerView
    private lateinit var travelCardsList: List<CardTravel>
    lateinit var usernames : List<String>
    lateinit var userImages : List<Int>
    lateinit var travelImages : List<Int>
    lateinit var travelNames : List<String>
    lateinit var travelAffinities : List<String>
    lateinit var travelLikes : List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelCardsList = listOf(
            //insert elements here
        )
        usernames = listOf(
            //insert elements here
        )
        userImages = listOf(
            //insert elements here
        )
        travelImages = listOf(
            //insert elements here
        )
        travelNames = listOf(
            //insert elements here
        )
        travelAffinities = listOf(
            //insert elements here
        )
        travelLikes = listOf(
            //insert elements here
        )

        travelCardsRecyclerView = findViewById(R.id.travelCardsRecyclerView)
        travelCardsRecyclerView.layoutManager = LinearLayoutManager(this)
        travelCardsRecyclerView.setHasFixedSize(true)

        travelCardsList = listOf<CardTravel>()

        //get data method for storing data into travelCardsList

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}