package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.ui.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import com.example.aitravelplanner.utils.notifyObserver
import javax.inject.Inject

class DashboardViewModel @Inject constructor() : TravelViewModel() {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        executeWithLoadingSuspend(block ={
            if(currentUser.value != null) {
                TravelCardsSingleton.setTravelCards(currentUser.value!!.idUser)
                setTravelCards()
            }
        })
    }

    override suspend fun setTravelCards() {
        TravelCardsSingleton.travelCardsList.observeForever { it ->
            val newSearchedTravelList = arrayListOf<CardTravel>()
            val newCardList = arrayListOf<CardTravel>()
            for (cardTravel in it) {
                if (cardTravel.isShared) {
                    newCardList.add(cardTravel)
                    if(searchText.value == "") {
                        newSearchedTravelList.add(cardTravel)
                    }
                }
            }
            _cardsList.value = newCardList
            if(searchText.value == "")
                _searchedCardsList.value = newSearchedTravelList
            else
                _searchedCardsList.notifyObserver()
        }
    }

    fun search(){
        executeWithLoading(block = {
            _searchedCardsList.value!!.clear()

            for(card in _cardsList.value!!) {
                if (searchText.value.toString().lowercase() in card.travelName.lowercase())
                    _searchedCardsList.value!!.add(card)
                _searchedCardsList.notifyObserver()
            }
        })
    }

    override fun clickLike(){
        super.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }
}