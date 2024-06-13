package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName
import io.mockk.mockk
import java.text.SimpleDateFormat
import java.util.Date

class TravelRepositoryMock : ITravelRepository {
    private val dateString = "10-10-2002"
    private val dateFormat = SimpleDateFormat("dd-MM-yyyy")
    private val date: Date? = dateFormat.parse(dateString)
    private val travel1 = Travel(idTravel = "1", idUser = "idUserTest1", info = "Info", name = "test1", isShared = true, timestamp = date, numberOfLikes = 0, imageUrl = "imageURL", stageList = ArrayList<Stage>(), isLiked = false)
    private val travel2 = Travel(idTravel = "2", idUser = "idUserTest1", info = "Info", name = "Milano", isShared = true, timestamp = date, numberOfLikes = 0, imageUrl = "imageURL", stageList = ArrayList<Stage>(), isLiked = true)
    private val travel3 = Travel(idTravel = "3", idUser = "idUserTest1", info = "Info", name = "New York", isShared = true, timestamp = date, numberOfLikes = 10, imageUrl = "imageURL", stageList = ArrayList<Stage>(), isLiked = false)
    private val travels = mutableListOf(travel1, travel2, travel3)
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

    override suspend fun getSharedTravels(
        idUser: String,
        resetPage: Boolean,
        searchText: String
    ): ArrayList<Travel> {
        TODO("Not yet implemented")
    }

    override suspend fun getTravelById(idTravel: String, idUser: String): Travel? {
        return travels.find { it.idTravel == idTravel && it.idUser == idUser }
    }

    override suspend fun getFilteredStagesByCity(filter: String, city: String): ArrayList<Stage> {
        return ArrayList(stages.filter { it.city == city && it.name.contains(filter) })
    }

    override suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage> {
        return travels.find { it.idTravel == idTravel }?.stageList ?: ArrayList()
    }

    override suspend fun getTravelsCreatedByUser(user: User): ArrayList<Travel> {
        return ArrayList(travels.filter { it.idUser == mockk(user.idUser) })
    }

    override suspend fun isTravelLikedByUser(idTravel: String, idUser: String): Boolean {
        return travels.find { it.idTravel == idTravel }?.isLiked ?: false
    }

    override suspend fun getTravelsBySearchText(
        idUser: String,
        searchText: String
    ): ArrayList<Travel> {
        TODO("Not yet implemented")
    }

    override suspend fun getTravelsByUser(idUser: String): ArrayList<Travel> {
        TODO("Not yet implemented")
    }
}