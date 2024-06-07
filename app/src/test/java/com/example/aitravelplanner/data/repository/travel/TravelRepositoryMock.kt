package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import io.mockk.mockk

class TravelRepositoryMock : ITravelRepository {
    private val travels = mutableListOf<Travel>()
    private val stages = mutableListOf<Stage>()

    override suspend fun setTravel(travel: Travel) {
        travels.add(travel)
    }

    override suspend fun setStageByTravel(idTravel: String, stage: Stage) {
        val travel = travels.find { it.idTravel == idTravel }
        travel?.stageList?.add(stage)
    }

    override suspend fun setTravelToShared(idTravel: String) {
        val travel = travels.find { it.idTravel == idTravel }
        travel?.isShared = true
    }

    override suspend fun getTravels(): ArrayList<Travel> {
        return ArrayList(travels)
    }

    override suspend fun getSharedTravels(idUser: String): ArrayList<Travel> {
        return ArrayList(travels.filter { it.isShared == true })
    }

    override suspend fun getTravelById(idTravel: String, idUser: String): Travel? {
        return travels.find { it.idTravel == idTravel && it.idUser == mockk(idUser) }
    }

    override suspend fun getFilteredStagesByCity(filter: String, city: String): ArrayList<Stage> {
        return ArrayList(stages.filter { it.city == city && it.name.contains(filter) })
    }

    override suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage> {
        return travels.find { it.idTravel == idTravel }?.stageList ?: ArrayList()
    }

    override suspend fun getTravelsCreatedByUser(user: User): ArrayList<Travel> {
        return ArrayList(travels.filter { it.idUser == mockk( user.idUser) })
    }
}