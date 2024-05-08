package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository

class TravelRepository: ITravelRepository, BaseRepository() {
    override suspend fun getTravels(): ArrayList<Travel> {
        TODO("Not yet implemented")
    }

    override suspend fun setTravel(travel: Travel) {
        TODO("Not yet implemented")
    }

    override suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel> {
        TODO("Not yet implemented")
    }

    override suspend fun setLikedTravelByUser(user: User, travel: Travel) {
        TODO("Not yet implemented")
    }

}