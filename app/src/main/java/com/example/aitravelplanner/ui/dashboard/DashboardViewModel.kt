package com.example.aitravelplanner.ui.dashboard

import android.util.Log
import androidx.compose.ui.text.toLowerCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

class DashboardViewModel : ViewModel() {
    private var _cardsList = MutableLiveData(arrayListOf<CardTravel>())
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    private var usernames : ArrayList<String> = arrayListOf()
    private var userImages : ArrayList<String> = arrayListOf()
    private var travelImages : ArrayList<String> = arrayListOf()
    private var travelNames : ArrayList<String> = arrayListOf()
    private var travelAffinities : ArrayList<String> = arrayListOf()
    private var travelLikes : ArrayList<Int> = arrayListOf()
    private var timestamps : ArrayList<String> = arrayListOf()

    init{
        usernames = arrayListOf("Samuele", "Paolo", "Daniele", "Maria")
        userImages = arrayListOf("https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180")
        travelImages = arrayListOf("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg")
        travelNames = arrayListOf("Roma", "Roma", "Roma", "Roma")
        travelAffinities = arrayListOf("100", "100", "100", "100")
        travelLikes = arrayListOf(0, 0, 0, 0)
        timestamps = arrayListOf("12-10-2022", "12-10-2022", "12-10-2022", "12-10-2022")

        setTravelCards()
    }

    private fun setTravelCards(){
        for (i in (usernames.indices)) {
            val card = CardTravel(
                username = usernames[i], userImage = userImages[i],
                travelImage = travelImages[i], travelName = travelNames[i],
                affinityPerc = travelAffinities[i], travelLikes = travelLikes[i],
                timestamp = timestamps[i], isLiked = false
            )

            _cardsList.value!!.add(card)
        }
        _searchedCardsList.value!!.addAll(_cardsList.value!!)
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

        for(card in _cardsList.value!!){
            if(searchText.value.toString().lowercase() in card.travelName.lowercase())
                _searchedCardsList.value!!.add(card)
        }
        _searchedCardsList.value = _searchedCardsList.value
    }

}