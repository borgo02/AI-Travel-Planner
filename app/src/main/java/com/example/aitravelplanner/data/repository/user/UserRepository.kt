package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository

class UserRepository: IUserRepository, BaseRepository() {
    override suspend fun setUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun setSharedTravelByUser(user: User, travel: Travel){
        TODO("Not yet implemented")
    }
    override suspend fun getUsers(): ArrayList<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getTravelsByUser(user: User): ArrayList<Travel> {
        TODO("Not yet implemented")
    }

    override suspend fun getLikedTravelsByUSer(user: User): ArrayList<Travel> {
        TODO("Not yet implemented")
    }
}