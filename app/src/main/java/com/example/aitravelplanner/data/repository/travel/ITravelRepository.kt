package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User

interface ITravelRepository {

    suspend fun getTravels(): ArrayList<Travel>
    suspend fun setTravel(travel: Travel)
    suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel>
    suspend fun setLikedTravelByUser(user: User, travel: Travel)

}