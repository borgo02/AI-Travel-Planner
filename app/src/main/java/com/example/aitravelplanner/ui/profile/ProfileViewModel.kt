package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

class ProfileViewModel : ViewModel() {
    private var travelRepository: TravelRepository = TravelRepository()
    private var userRepository: UserRepository = UserRepository()

    private var _cardsList = MutableLiveData(arrayListOf<CardTravel>())
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList

    init{

        setTravelCards()
    }

    private fun setTravelCards(){

    }

    fun isShared(): Boolean {
        return false
    }
}