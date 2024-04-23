package com.example.aitravelplanner.viewmodel.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.model.user.User
import com.example.aitravelplanner.data.repository.user.UserRepository

class UserViewModel(
    val repository: UserRepository
): ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    fun getUsers(){
        _users.value = repository.getUsers()
    }

}