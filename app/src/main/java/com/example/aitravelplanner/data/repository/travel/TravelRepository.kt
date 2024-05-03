package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.example.aitravelplanner.data.repository.user.IUserRepository

class TravelRepository: ITravelRepository, BaseRepository() {
    //Use "db" for referencing to the Firebase DB
    override fun getTravels(): List<Travel> {
        //Here get data from firebase
        return arrayListOf()
    }
}