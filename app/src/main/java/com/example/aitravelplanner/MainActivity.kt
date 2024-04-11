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
    private lateinit var travelCardsList: ArrayList<CardTravel>
    lateinit var usernames : ArrayList<String>
    lateinit var userImages : ArrayList<Int>
    lateinit var travelImages : ArrayList<Int>
    lateinit var travelNames : ArrayList<String>
    lateinit var travelAffinities : ArrayList<String>
    lateinit var travelLikes : ArrayList<String>

    //get data method for storing data into travelCardsList
    private fun getTravelCards(){

        for(i in (travelCardsList.indices)){
            val card = CardTravel(  usernames[i], userImages[i],
                travelImages[i], travelNames[i],
                travelAffinities[i], travelLikes[i])
            travelCardsList.add(card)
        }

        travelCardsRecyclerView.adapter = CardAdapter(travelCardsList)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelCardsList = arrayListOf(
            //insert elements here
        )
        usernames = arrayListOf(
            //insert elements here
        )
        userImages = arrayListOf(
            //insert elements here
        )
        travelImages = arrayListOf(
            //insert elements here
        )
        travelNames = arrayListOf(
            //insert elements here
        )
        travelAffinities = arrayListOf(
            //insert elements here
        )
        travelLikes = arrayListOf(
            //insert elements here
        )

        travelCardsRecyclerView = findViewById(R.id.travelCardsRecyclerView)
        travelCardsRecyclerView.layoutManager = LinearLayoutManager(this)
        travelCardsRecyclerView.setHasFixedSize(true)

        travelCardsList = arrayListOf<CardTravel>()

        // populate cards
        getTravelCards()

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