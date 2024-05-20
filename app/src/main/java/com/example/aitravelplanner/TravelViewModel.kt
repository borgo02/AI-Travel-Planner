package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

abstract class TravelViewModel: BaseViewModel() {
    protected val travelRepository: TravelRepository = TravelRepository()
    protected val userRepository: UserRepository = UserRepository()
    protected var _cardsList = MutableLiveData(arrayListOf<CardTravel>())


    protected var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    protected abstract suspend fun setTravelCards(travels: ArrayList<Travel>)

    fun loadSelectedTravel(cardTravel: CardTravel){
        _selectedTravel.value = cardTravel
    }

}