package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp
import com.google.firebase.firestore.toObject

class UserRepository: IUserRepository, BaseRepository() {
    private var user: User? = null

    override suspend fun setUser(user: User) {
        db.collection("users").document(user.idUser).set(user).await()
    }

    override suspend fun setSharedTravelByUser(travel: Travel) {
        val travelRef = db.collection("travels").document(travel.idTravel)
        val travelData = travelRef.get().await().toObject(Travel::class.java)

        if (travelData != null)
            travelRef.update("sharedTravelList", true)

    }

    override suspend fun setLikedTravelByUser(user: User, travel: Travel) {
        val newLikesRef = db.collection("likes").document()
        val newLike = Likes(newLikesRef.id, travel.idTravel, user.idUser, Timestamp.now().toDate())

        newLikesRef.set(newLike).await()
    }

    override suspend fun getUserById(idUser: String): User? {
        val userRef = db.collection("users").document(idUser).get().await()
        user = userRef.toObject(User::class.java)
        return user
    }

    override suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel> {
        val travelRef = db.collection("travels").whereEqualTo("idUser", user.idUser).get().await()
        val sharedTravelList: ArrayList<Travel> = arrayListOf()

        for(travel in travelRef.documents){
            val travelData = travel.toObject(Travel::class.java)
            if(travelData != null && travelData.isShared)
                sharedTravelList.add(travelData)
        }

        return sharedTravelList
    }

    override suspend fun getNotSharedTravelsByUser(user: User): ArrayList<Travel> {
        val travelRef = db.collection("travels").whereEqualTo("idUser", user.idUser).get().await()
        val notSharedTravelList: ArrayList<Travel> = arrayListOf()

        for(travel in travelRef.documents){
            val travelData = travel.toObject(Travel::class.java)
            if(travelData != null && !travelData.isShared)
                notSharedTravelList.add(travelData)
        }

        return notSharedTravelList
    }

    override suspend fun getUsers(): ArrayList<User> {
        val users = db.collection("users").get().await()
        val userList: ArrayList<User> = arrayListOf()

        for (doc in users.documents) {
            val userData = doc.toObject(User::class.java)
            if (userData != null)
                userList.add(userData)
        }

        return userList
    }

    override suspend fun getTravelsByUser(user: User): ArrayList<Travel> {
        val travels = db.collection("travels").whereEqualTo("idUser", user.idUser).get().await()
        val travelList: ArrayList<Travel> = arrayListOf()

        for(travel in travels.documents){
            val travelData = travel.toObject(Travel::class.java)
            if (travelData != null)
                travelList.add(travelData)
        }

        return travelList
    }

    override suspend fun getLikedTravelsByUser(user: User): ArrayList<Travel> {
        val likeRef = db.collection("likes").whereEqualTo("idUser", user.idUser).get().await()
        val likedTravelList: ArrayList<Travel> = arrayListOf()

        for(like in likeRef.documents){
            val likeData = like.toObject(Likes::class.java)
            if(likeData != null) {
                val idTravel = likeData.idTravel
                val travelRef = db.collection("travels").document(idTravel).get().await()
                val travelData = travelRef.toObject(Travel::class.java)

                if(travelData != null)
                    likedTravelList.add(travelData)
            }
        }

        return likedTravelList
    }
}