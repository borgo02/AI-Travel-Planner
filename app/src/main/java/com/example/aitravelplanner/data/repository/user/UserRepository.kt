package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.BaseRepository

class UserRepository: IUserRepository, BaseRepository() {
    //Use "db" for referencing to the Firebase DB
    override fun getUsers(): List<User> {
        //Here get data from firebase
        return arrayListOf()
    }
}