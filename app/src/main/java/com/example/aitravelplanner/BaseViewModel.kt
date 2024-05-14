package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.model.User

public open class BaseViewModel : ViewModel() {
    private val _user = MutableLiveData<User>().apply {
        value = User("Initialized")
    }
    val user: LiveData<User> = _user
}