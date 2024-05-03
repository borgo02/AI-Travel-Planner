package com.example.aitravelplanner.viewmodel.travel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository

class TravelViewModel(
    val repository: TravelRepository
): ViewModel() {

    private val _users = MutableLiveData<List<Travel>>()
    val users: LiveData<List<Travel>>
        get() = _users

    fun getUsers(){
        _users.value = repository.getTravels()
    }

}