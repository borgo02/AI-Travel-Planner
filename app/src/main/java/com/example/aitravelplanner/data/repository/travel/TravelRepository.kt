package com.example.aitravelplanner.data.repository.travel

import android.util.Log
import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.tasks.await

class TravelRepository: ITravelRepository, BaseRepository() {
    private val usersCollectionRef: CollectionReference = db.collection("users")
    var travelsCollectionReference: CollectionReference = db.collection("travels")

    /** Inserisce un nuovo viaggio nel database Firestore
     *
     */
    override suspend fun setTravel(travel: Travel) {
        val documentReference = travelsCollectionReference.document()
        travel.idTravel = documentReference.id
        documentReference.set(travel).await()
        travel.stageList?.let {
            for (stage in it)
                this.setStageByTravel(travel.idTravel!!, stage)
        }
    }

    /** Modifica il valore "shared" di un viaggio da false a true.
     *
     * In questo modo il viaggio sarà pubblicato in dashboard
     */
    override suspend fun setTravelToShared(idTravel: String) {
        val travelDoc = travelsCollectionReference.document(idTravel)
        val travelRef = travelDoc.get().await()
        if(travelRef.exists()) {
            val newShareData = mapOf(
                "shared" to true
            )
            travelDoc.update(newShareData)
        }
    }

    /** Inserisce una nuova tappa ad un viaggio specifico
     *
     */
    override suspend fun setStageByTravel(idTravel: String, stage: Stage){
        val travelDoc = travelsCollectionReference.document(idTravel)
        val travelRef = travelDoc.get().await()
        if(travelRef.exists())
            travelDoc.collection("stages").add(stage)
    }

    /** Ritorna tutti i viaggi presenti nel database
     *
     */
    override suspend fun getTravels(): ArrayList<Travel>{
        val travelsDoc = travelsCollectionReference.get().await()
        val travelList: ArrayList<Travel> = arrayListOf()
        for (doc in travelsDoc.documents) {
            val idTravel = doc.id
            val travelData = this.getTravelById(idTravel, "")
            if (travelData != null)
                    travelList.add(travelData)
        }

        return travelList
    }

    /** Ritorna tutti i viaggi presenti nel database che sono stati pubblicati
     *
     */
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

    /** Ritorna un viaggio identificato da uno specifico id
     *
     */
    override suspend fun getTravelById(idTravel: String, idUser: String): Travel?{
        var isLiked: Boolean
        val doc = travelsCollectionReference.document(idTravel).get().await()
        return if (doc.exists()) {
            val idUserRef = doc.getString("idUser")
            val info = doc.getString("info")
            val name = doc.getString("name")
            val isShared = doc.getBoolean("shared")
            val numberOfLikes = doc.getLong("numberOfLikes")?.toInt()
            val imageUrl = doc.getString("imageUrl")
            val timestamp = doc.getTimestamp("timestamp")?.toDate()
            isLiked = if(idUser != "")
                this.isTravelLikedByUser(idTravel, idUser)
            else
                false
            val stages = this.getStagesByTravel(idTravel)
            Travel(idTravel, idUserRef, info, name, isShared, timestamp, numberOfLikes, imageUrl, stages, isLiked)
        } else
            null
    }

    /** Ritorna tutte le tappe collegate ad uno specifico viaggio
     *
     */
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

    /** Ritorna tutte le tappe filtrate per città e per una "stringa filtro" inserita dall'utente
     *
     */
    override suspend fun getFilteredStagesByCity(filter: String, city: String): ArrayList<Stage>{
        val travels = this.getTravels()
        val stageList: ArrayList<Stage> = arrayListOf()
        for(travel in travels) {
            val stages = getStagesByTravel(travel.idTravel!!)
            for (stage in stages)
                if (stage.city.lowercase() == city.lowercase() && filter.lowercase() in stage.name.lowercase())
                    stageList.add(stage)
        }

        return stageList
    }

    /** Ritorna tutti i viaggi creati da uno specifico utente
     *
     */
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

    /** Presi come input gli id di un viaggio e di utente, ritorna:
     *
     * - false se l'utente non ha messo like al viaggio
     *
     * - true altrimenti.
     *
     */
    override suspend fun isTravelLikedByUser(idTravel: String, idUser: String): Boolean {
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