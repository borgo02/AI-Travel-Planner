package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.BaseRepository
import kotlinx.coroutines.tasks.await

class TravelRepository: ITravelRepository, BaseRepository() {
    override suspend fun setTravel(travel: Travel) {
        db.collection("travels").document().set(travel).await()
    }

    override suspend fun setTravelToShared(idTravel: String) {
        val travelDoc = db.collection("travels").document(idTravel)
        val travelRef = travelDoc.get().await()

        if(travelRef.exists()) {
            val newShareData = mapOf(
                "isShared" to true
            )
            travelDoc.update(newShareData)
        }
    }

    override suspend fun setStageByTravel(idTravel: String, stage: Stage){
        val travelDoc = db.collection("travels").document(idTravel)
        val travelRef = travelDoc.get().await()

        if(travelRef.exists()){
            travelDoc.collection("stages").add(stage)
        }

    }

    override suspend fun getTravels(): ArrayList<Travel> {
        val travelsDoc = db.collection("travels").get().await()
        val travelList: ArrayList<Travel> = arrayListOf()
        for (doc in travelsDoc.documents) {
            val idTravel = doc.id
            val travelData = this.getTravelById(idTravel)!!
            travelList.add(travelData)
        }

        return travelList
    }

    override suspend fun getTravelById(idTravel: String): Travel? {
        val doc = db.collection("travels").document(idTravel).get().await()
        return if (doc.exists()) {
            val idUserReferencePath = doc.getDocumentReference("idUser")?.path
            val idUser = idUserReferencePath?.substringAfterLast("/")
            val info = doc.getString("info")
            val name = doc.getString("name")
            val isShared = doc.getBoolean("isShared")
            val numberOfLikes = doc.getLong("numberOfLikes")?.toInt()
            val imageUrl = doc.getString("imageUrl")
            val timestamp = doc.getTimestamp("timestamp")?.toDate()
            val isLiked = isTravelLikedByUser(idTravel, idUser!!)
            val stages = this.getStagesByTravel(idTravel)
            Travel(idTravel, idUser, info, name, isShared, timestamp, numberOfLikes, imageUrl, stages, isLiked)
        } else
            null
    }

    override suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage> {
        val stagesRef = db.collection("travels").document(idTravel).collection("stages").get().await()
        val stagesList: ArrayList<Stage> = arrayListOf()

        for(stage in stagesRef.documents){
            val idStage = stage.id
            val name = stage.getString("name")
            val imageUrl = stage.getString("imageUrl")
            val city = stage.getString("city")
            val description = stage.getString("description")
            val position = stage.get("position")!!.toString()

            val stageItem = Stage(idStage, name!!, imageUrl!!, city!!, description!!, position.toInt())
            stagesList.add(stageItem)
        }

        return stagesList
    }

    override suspend fun isTravelLikedByUser(idTravel: String, idUser: String): Boolean {
        val likesRef = db.collection("users").document(idUser).collection("likedTravels").get().await()
        var isTravelLiked: Boolean = false
        for(like in likesRef.documents){
            val idTravelReferencePath = like.getDocumentReference("idTravel")?.path
            val idTravelDoc = idTravelReferencePath?.substringAfterLast("/")

            if(idTravelDoc.toString() == idTravel)
                isTravelLiked = true
        }

        return isTravelLiked
    }

}