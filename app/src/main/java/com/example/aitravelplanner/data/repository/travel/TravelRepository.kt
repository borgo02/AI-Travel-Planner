package com.example.aitravelplanner.data.repository.travel

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.google.firebase.firestore.toObject

class TravelRepository: ITravelRepository, BaseRepository() {
    private lateinit var travelList: ArrayList<Travel>
    private lateinit var sharedTravelList: ArrayList<Travel>
    private lateinit var currentLikedTravelList: ArrayList<Travel>
    override suspend fun getTravels(): ArrayList<Travel> {
        val travelRef = db.collection("travels")

        travelRef.get().addOnSuccessListener { travels ->
            for (doc in travels.documents) {
                val travelData = doc.toObject(Travel::class.java)
                if(travelData != null) {
                    travelList.clear()
                    travelList.add(travelData)
                }
            }
        }

        return travelList
    }

    override suspend fun setTravel(travel: Travel) {
        db.collection("travels").document(travel.idTravel).set(travel)
    }

    //Get shared travels by a user
    override suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel> {
        val userRef = db.collection("users").document(user.idUser)

        userRef.get().addOnSuccessListener { doc ->
            val userData = doc.toObject(User::class.java)
            if(userData != null) {
                sharedTravelList.clear()
                sharedTravelList = userData.sharedTravelList
            }
        }

        return sharedTravelList
    }

    //Add a new travel in the list of travels liked by the user
    override suspend fun setLikedTravelByUser(user: User, travel: Travel) {
        val userRef = db.collection("users").document(user.idUser)
        val travelRef = db.collection("travel").document(travel.idTravel)

        travelRef.get().addOnSuccessListener { doc ->
            if(doc.exists()){
                val travelData = doc.toObject(Travel::class.java)
                if(travelData != null){
                    userRef.get().addOnSuccessListener { document ->
                        if (document.exists()) {
                            val userData = document.toObject(User::class.java)
                            if (userData != null) {
                                travelData.numberOfLikes += 1

                                currentLikedTravelList.clear()
                                currentLikedTravelList = userData.likedTravelList
                                currentLikedTravelList.add(travel)

                                userRef.update("likedTravelList", currentLikedTravelList)
                                travelRef.update("numberOfLikes", travelData.numberOfLikes)
                            }
                        }
                    }
                }
            }
        }
    }

}