package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User

interface IUserRepository {
    suspend fun setUser(user: User)
    suspend fun setSharedTravelByUser(user: User, travel: Travel)
    suspend fun getUsers(): List<User>
    suspend fun getTravelsByUser(user: User): ArrayList<Travel>
    suspend fun getLikedTravelsByUSer(user: User): ArrayList<Travel>

}