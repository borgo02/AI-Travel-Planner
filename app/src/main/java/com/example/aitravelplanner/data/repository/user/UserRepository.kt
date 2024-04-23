package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.user.User
import com.example.aitravelplanner.data.repository.BaseRepository

class UserRepository: IUserRepository {

    val repository = BaseRepository()
    val db = repository.databaseRef

    override fun getUsers(): List<User> {
        //Here get data from firebase
        return arrayListOf()
    }
}