package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.BaseRepository

class TravelRepository: ITravelRepository {

    val repository = BaseRepository()
    val db = repository.databaseRef

    override fun getTravels(): List<Travel> {
        //Here get data from firebase
        return arrayListOf()
    }
}