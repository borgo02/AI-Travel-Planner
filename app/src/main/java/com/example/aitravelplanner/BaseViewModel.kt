package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.model.User

public open class BaseViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user

    public fun setUser(newUser: User) {
        _user.value = newUser
    }
}