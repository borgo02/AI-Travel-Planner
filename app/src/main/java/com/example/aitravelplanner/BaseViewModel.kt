package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

public open class BaseViewModel : ViewModel() {
    private val _user = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val user: LiveData<String> = _user
}