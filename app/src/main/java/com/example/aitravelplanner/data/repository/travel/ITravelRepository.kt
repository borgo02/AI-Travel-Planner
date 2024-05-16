package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User

interface ITravelRepository {
    suspend fun setTravel(travel: Travel)
    suspend fun setStageByTravel(idTravel: String, stage: Stage)
    suspend fun setTravelToShared(idTravel: String)
    suspend fun getTravels(): ArrayList<Travel>
    suspend fun getTravelById(idTravel: String): Travel?
    suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage>
    suspend fun getTravelsByUser(user: User): ArrayList<Travel>
}