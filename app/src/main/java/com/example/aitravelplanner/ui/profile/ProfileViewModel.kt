package com.example.aitravelplanner.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : TravelViewModel() {
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _sharedTravelList = MutableLiveData(arrayListOf<CardTravel>())
    val sharedTravelList: LiveData<ArrayList<CardTravel>>
        get() = _sharedTravelList
    val isProfileLoading = MutableLiveData<Boolean>(false)


    init{
        viewModelScope.launch {
            isProfileLoading.value = true
            travelCardsSingleton.setTravelCards(currentUser.value!!.idUser)
            setTravelCards()
            isProfileLoading.value = false
        }
    }

    override suspend fun setTravelCards() {
        travelCardsSingleton.travelCardsList.observeForever { it ->
            val newSharedTravelList =arrayListOf<CardTravel>()
            val cardList = arrayListOf<CardTravel>()
            for (cardTravel in it) {
                if(cardTravel.userId == currentUser.value!!.idUser) {
                    cardList.add(cardTravel)
                    if(cardTravel.isShared)
                        newSharedTravelList.add(cardTravel)
                }
            }
            _cardsList.value = cardList
            _sharedTravelList.value = newSharedTravelList
        }
    }


    fun shareTravel(cardTravel: CardTravel){
        cardTravel.isShared = true
        MainScope().launch {
            travelRepository.setTravelToShared(cardTravel.travelId)
        }
        travelCardsSingleton.notifyChanges()
    }

    override fun clickLike(){
        super.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }
}