package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import kotlinx.coroutines.launch
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository

class DashboardViewModel : ViewModel() {
    private var travelRepository: TravelRepository = TravelRepository()
    private var userRepository: UserRepository = UserRepository()

    private var _cardsList = MutableLiveData(arrayListOf<Travel>())
    val cardsList: LiveData<ArrayList<Travel>>
        get() = _cardsList
    private var _searchedCardsList = MutableLiveData(arrayListOf<Travel>())
    val searchedCardsList: LiveData<ArrayList<Travel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        viewModelScope.launch{

        }

        setTravelCards()
    }

    private fun setTravelCards(){

    }

    fun isLiked(cardTravel: CardTravel): Boolean{
        cardTravel.isLiked = !cardTravel.isLiked
        if(cardTravel.isLiked) {
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        }
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1
        return cardTravel.isLiked
    }

    fun search(){
        _searchedCardsList.value!!.clear()

        for(travel in _cardsList.value!!){
            if(searchText.value.toString().lowercase() in travel.name!!.lowercase())
                _searchedCardsList.value!!.add(travel)
        }
        _searchedCardsList.value = _searchedCardsList.value
    }

}