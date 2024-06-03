package com.example.aitravelplanner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.EventBus
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class TravelViewModel@Inject constructor() : BaseViewModel() {
    protected var _cardsList = MutableLiveData(arrayListOf<CardTravel>())


    protected var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    protected abstract suspend fun setTravelCards(travels: ArrayList<Travel>)

    fun loadSelectedTravel(cardTravel: CardTravel){
        _selectedTravel.value = cardTravel
    }

    open fun isLiked(cardTravel: CardTravel, vmReference: String): Boolean {
        cardTravel.isLiked = !cardTravel.isLiked
        if (cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1

        MainScope().launch {
            userRepository.updateLikedTravelByUser(currentUser.value!!.idUser, cardTravel.travelId, cardTravel.isLiked)
        }

        if(vmReference == "dashboard")
            EventBus.notifyDashboardDataChanged()
        else if(vmReference == "shared_travels")
            EventBus.notifyProfileDataChanged()

        return cardTravel.isLiked
    }

    abstract fun clickLike()
}