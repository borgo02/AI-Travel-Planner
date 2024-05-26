package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

class TravelRepository: ITravelRepository, BaseRepository() {
    private val usersCollectionRef: CollectionReference = db.collection("users")
    private val travelsCollectionReference: CollectionReference = db.collection("travels")
    override suspend fun setTravel(travel: Travel) {
        travelsCollectionReference.document().set(travel).await()
    }

    override suspend fun setTravelToShared(idTravel: String) {
        val travelDoc = travelsCollectionReference.document(idTravel)
        val travelRef = travelDoc.get().await()
        if(travelRef.exists()) {
            val newShareData = mapOf(
                "isShared" to true
            )
            travelDoc.update(newShareData)
        }
    }

    override suspend fun setStageByTravel(idTravel: String, stage: Stage){
        val travelDoc = travelsCollectionReference.document(idTravel)
        val travelRef = travelDoc.get().await()
        if(travelRef.exists()){
            travelDoc.collection("stages").add(stage)
        }

    }

    override suspend fun getSharedTravels(idUser: String): ArrayList<Travel>{
        val travelsDoc = travelsCollectionReference.get().await()
        val travelList: ArrayList<Travel> = arrayListOf()
        for (doc in travelsDoc.documents) {
            val idTravel = doc.id
            val travelData = this.getTravelById(idTravel, idUser)
            if (travelData != null) {
                if(travelData.isShared == true)
                    travelList.add(travelData)
            }
        }

        return travelList
    }

    override suspend fun getTravelById(idTravel: String, idUser: String): Travel?{
        val doc = travelsCollectionReference.document(idTravel).get().await()
        return if (doc.exists()) {
            val idUserReferencePath = doc.getDocumentReference("idUser")?.path
            val idUserRef = idUserReferencePath?.substringAfterLast("/")
            val info = doc.getString("info")
            val name = doc.getString("name")
            val isShared = doc.getBoolean("isShared")
            val numberOfLikes = doc.getLong("numberOfLikes")?.toInt()
            val imageUrl = doc.getString("imageUrl")
            val timestamp = doc.getTimestamp("timestamp")?.toDate()
            val isLiked = this.isTravelLikedByUser(idTravel, idUser)
            val stages = this.getStagesByTravel(idTravel)
            Travel(idTravel, idUserRef, info, name, isShared, timestamp, numberOfLikes, imageUrl, stages, isLiked)
        } else
            null
    }

    override suspend fun getStagesByTravel(idTravel: String): ArrayList<Stage> {
        val stagesRef = travelsCollectionReference.document(idTravel).collection("stages").get().await()
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

    override suspend fun getTravelsCreatedByUser(user: User): ArrayList<Travel> {
        val travels = travelsCollectionReference.whereEqualTo("idUser", user.idUser).get().await()
        val travelList: ArrayList<Travel> = arrayListOf()
        for(travel in travels.documents){
            val travelData = travel.toObject(Travel::class.java)
            if (travelData != null)
                travelList.add(travelData)
        }

        return travelList
    }

    private suspend fun isTravelLikedByUser(idTravel: String, idUser: String): Boolean {
        return true;
        val likesRef = usersCollectionRef.document(idUser).collection("likedTravels").get().await()
        var isTravelLiked: Boolean = false
        for(like in likesRef.documents){
            val idTravelReferencePath = like.getDocumentReference("idTravel")!!.path
            val idTravelDoc = idTravelReferencePath.substringAfterLast("/")
            if(idTravelDoc == idTravel) {
                isTravelLiked = true
                break
            }
        }

        return isTravelLiked
    }
}