package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository

class UserRepository: IUserRepository, BaseRepository() {
    private lateinit var userList: ArrayList<User>
    private lateinit var travelList: ArrayList<Travel>
    private lateinit var likedTravelList: ArrayList<Travel>
    private lateinit var sharedTravelList: ArrayList<Travel>
    override suspend fun setUser(user: User) {
        db.collection("users").document(user.idUser).set(user)
    }

    override suspend fun setSharedTravelByUser(user: User, travel: Travel) {
        val userRef = db.collection("users").document(user.idUser)

        userRef.get().addOnSuccessListener { doc ->
            val userData = doc.toObject(User::class.java)
            if (userData != null) {
                sharedTravelList = userData.sharedTravelList
                sharedTravelList.add(travel)

                userRef.update("sharedTravelList", sharedTravelList)
            }
        }
    }

    override suspend fun getUsers(): ArrayList<User> {
        val userRef = db.collection("users")

        userRef.get().addOnSuccessListener { users ->
            for (doc in users.documents) {
                val userData = doc.toObject(User::class.java)
                if (userData != null) {
                    userList.clear()
                    userList.add(userData)
                }
            }
        }

        return userList
    }

    override suspend fun getTravelsByUser(user: User): ArrayList<Travel> {
        val userRef = db.collection("travels").document(user.idUser)

        userRef.get().addOnSuccessListener { doc ->
            val userData = doc.toObject(User::class.java)
            if (userData != null) {
                travelList.clear()
                travelList = userData.createdTravelList
            }
        }

        return travelList
    }

    override suspend fun getLikedTravelsByUser(user: User): ArrayList<Travel> {
        val userRef = db.collection("users").document(user.idUser)

        userRef.get().addOnSuccessListener { doc ->
            val userData = doc.toObject(User::class.java)
            if(userData != null){
                likedTravelList.clear()
                likedTravelList = userData.likedTravelList
            }
        }

        return likedTravelList
    }
}