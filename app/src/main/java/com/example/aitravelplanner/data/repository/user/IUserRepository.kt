package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Likes
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User

interface IUserRepository {
    suspend fun setUser(user: User)
    suspend fun updateLikedTravelByUser(idUser: String, idTravel: String, isLiked: Boolean)
    suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel>
    // return created travels but not shared by a given user
    suspend fun getNotSharedTravelsByUser(user: User): ArrayList<Travel>
    suspend fun getUsers(): ArrayList<User>
    suspend fun getUserById(idUser: String): User?
    suspend fun getUserByTravel(idTravel: String): User?
    // return the travels liked by a given user
    suspend fun getLikedTravelsByUser(idUser: String): ArrayList<Likes>
}