package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel

interface ITravelRepository {
    suspend fun setTravel(travel: Travel)
    suspend fun getTravels(): ArrayList<Travel>
    suspend fun getTravelById(idTravel: String): Travel?

}