package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.user.User

interface IUserRepository {

    fun getUsers(): List<User>

}