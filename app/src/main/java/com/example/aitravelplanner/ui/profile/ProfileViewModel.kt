package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import com.example.aitravelplanner.utils.notifyObserver
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : TravelViewModel() {
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _sharedTravelList = MutableLiveData(arrayListOf<CardTravel>())
    val sharedTravelList: LiveData<ArrayList<CardTravel>>
        get() = _sharedTravelList

    private var _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean>
        get() = _logout


    init{
        executeWithLoadingSuspend(block = {
            setTravelCards()
        })
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

    fun logout() {
        Firebase.auth.signOut()
        _logout.value = true
    }
}