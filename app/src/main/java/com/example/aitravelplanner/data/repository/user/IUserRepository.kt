package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.google.android.gms.tasks.Task

interface IUserRepository {
    // Add a user into the Firestore database, under "users" collection
    suspend fun setUser(user: User)
    // Get all travels by a given idUser as a String
    suspend fun getTravelsByUser(idUser: String): ArrayList<Travel>
    // Get only shared travels by a given idUser as a String
    suspend fun getSharedTravelsByUser(idUser: String): ArrayList<Travel>
    // Get only not yet shared travels by a given idUser as a String
    suspend fun getNotSharedTravelsByUser(idUser: String): ArrayList<Travel>
    // Get all the users in the Firestore database
    suspend fun getUsers(): ArrayList<User>
    // If exists, get a user with a specif Id
    suspend fun getUserById(idUser: String): User?
    // Get the owner user of a travel with specific idTravel
    suspend fun getUserByTravel(idTravel: String): User?
    // Get likes of a users, with idTravel and timestamp
    suspend fun getLikesByUser(idUser: String): ArrayList<Likes>
    // Update number of likes of a specific travel and user's liked travel list of
    suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean)
}