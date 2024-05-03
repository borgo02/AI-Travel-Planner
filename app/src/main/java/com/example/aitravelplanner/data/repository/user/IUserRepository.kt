package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.User

interface IUserRepository {

    fun getUsers(): List<User>

}