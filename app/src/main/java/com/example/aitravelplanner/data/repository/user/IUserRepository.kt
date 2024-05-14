package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User

interface IUserRepository {
    suspend fun setUser(user: User)
    suspend fun setSharedTravelByUser(travel: Travel)
    suspend fun setLikedTravelByUser(user: User, travel: Travel)

    suspend fun getUserById(idUser: String): User?
    suspend fun getSharedTravelsByUser(user: User): ArrayList<Travel>
    // return created travels but not shared by a given user
    suspend fun getNotSharedTravelsByUser(user: User): ArrayList<Travel>
    suspend fun getUsers(): ArrayList<User>
    // return travels shared and not shared by a given user
    suspend fun getTravelsByUser(user: User): ArrayList<Travel>
    // return the travels liked by a given user
    suspend fun getLikedTravelsByUser(user: User): ArrayList<Travel>

}