package com.example.aitravelplanner.data.repository.user

import android.util.Log
import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

class UserRepository: IUserRepository, BaseRepository() {
    private var travelRepository: TravelRepository = TravelRepository()
    //Visonato
    override suspend fun setUser(user: User) {
        db.collection("users").document().set(user).await()
    }

    //Visionato
    override suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean) {
        val travelRef = db.collection("travels").document(idTravel)
        val likedTravelsRef = db.collection("users").document(idUser).collection("likedTravels")
        if(!isLiked) {
            val like = Likes(null, travelRef, Timestamp.now())
            db.runTransaction{transaction ->
                val snapshot = transaction.get(travelRef)
                val newLikesValue = snapshot.getLong("numberOfLikes")!! + 1
                transaction.update(travelRef, "numberOfLikes", newLikesValue)
                likedTravelsRef.document().set(like)
            }
        }else{
            val likes = likedTravelsRef.get().await()
            for(like in likes.documents) {
                val idLike = like.id
                val idTravelReferencePath = like.getDocumentReference("idTravel")?.path
                val idTravelDoc = idTravelReferencePath?.substringAfterLast("/")

                if (idTravelDoc.toString() == idTravel) {
                    db.runTransaction{transaction ->
                        val snapshot = transaction.get(travelRef)
                        val newLikesValue = snapshot.getLong("numberOfLikes")!! - 1
                        transaction.update(travelRef, "numberOfLikes", newLikesValue)
                        likedTravelsRef.document().set(like)
                        likedTravelsRef.document(idLike).delete()
                    }
                }
            }

        }
    }

    //Da testare
    override suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel> {
        val travelRef = db.collection("travels").whereEqualTo("idUser", user.idUser).get().await()
        val sharedTravelList: ArrayList<Travel> = arrayListOf()

        for(travel in travelRef.documents){
            val travelData = travelRepository.getTravelById(travel.id)
            if(travelData != null && travelData.isShared!!)
                sharedTravelList.add(travelData)
        }

        return sharedTravelList
    }

    //Da testare
    override suspend fun getNotSharedTravelsByUser(user: User): ArrayList<Travel> {
        val travelRef = db.collection("travels").whereEqualTo("idUser", user.idUser).get().await()
        val notSharedTravelList: ArrayList<Travel> = arrayListOf()

        for(travel in travelRef.documents){
            val travelData = travel.toObject(Travel::class.java)
            if(travelData != null && !travelData.isShared!!)
                notSharedTravelList.add(travelData)
        }

        return notSharedTravelList
    }

    //Visionato
    override suspend fun getUsers(): ArrayList<User> {
        val userRef = db.collection("users")
        val users = userRef.get().await()
        val userList: ArrayList<User> = arrayListOf()

        for (doc in users.documents) {
            val idUser = doc.id
            val userData = this.getUserById(idUser)
            if(userData != null)
                userList.add(userData)
        }

        return userList
    }

    override suspend fun getUserById(idUser: String): User? {
        val userDoc = db.collection("users").document(idUser).get().await()
        var likedTravelList: ArrayList<Likes>

        return if(userDoc.exists()){
            val email = userDoc.getString("email")
            val fullname = userDoc.getString("fullname")
            val interests = userDoc.get("interests") as Map<*, *>
            updateLikedTravelByUser(idUser, "eZZEc2PhNkaNTM9vl9kK", false)
            likedTravelList = this.getLikedTravelsByUser(idUser)
            User(idUser, email, fullname, interests, likedTravelList)
        }else
            null
    }

    //Visionato
    override suspend fun getUserByTravel(idTravel: String): User? {
        val travelRef = db.collection("travels").document(idTravel).get().await()

        return if(travelRef.exists()){
            val idUserReferencePath = travelRef.getDocumentReference("idUser")?.path
            val idUser = idUserReferencePath?.substringAfterLast("/")!!
            val userRef = db.collection("users").document(idUser).get().await()
            if(userRef.exists())
                getUserById(idUser)
            else
                null
        } else
            null
    }

    //Da testare
    override suspend fun getLikedTravelsByUser(idUser: String): ArrayList<Likes> {
        val likesList: ArrayList<Likes> = arrayListOf()
        val likesRef = db.collection("users").document(idUser).collection("likedTravels").get().await()

        for(like in likesRef.documents){
            val idLike = like.id
            val idTravelReferencePath = like.getDocumentReference("idTravel")?.path
            val idTravel = idTravelReferencePath?.substringAfterLast("/")
            val timestamp = like.getTimestamp("timestamp")
            val travelRef = db.collection("travels").document(idTravel!!)

            val likeItem = Likes(idLike, travelRef, timestamp!!)
            likesList.add(likeItem)
        }

        return likesList
    }
}