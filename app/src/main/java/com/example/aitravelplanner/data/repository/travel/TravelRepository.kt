package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.BaseRepository
import kotlinx.coroutines.tasks.await

class TravelRepository: ITravelRepository, BaseRepository() {
    private var travel: Travel? = null

    override suspend fun getTravels(): ArrayList<Travel> {
        val travelsDoc = db.collection("travels").get().await()
        val travelList: ArrayList<Travel> = arrayListOf()

        for (doc in travelsDoc.documents) {
            val travelData = doc.toObject(Travel::class.java)
            if(travelData != null) {
                travelList.add(travelData)
            }
        }

        return travelList
    }

    override suspend fun getTravelById(idTravel: String): Travel? {
        val travelDoc = db.collection("travels").document(idTravel).get().await()

        if (travelDoc.exists())
            travel = travelDoc.toObject(Travel::class.java)
        else
            return null

        return travel
    }

    override suspend fun setTravel(travel: Travel) {
        db.collection("travels").document(travel.idTravel).set(travel).await()
    }

}