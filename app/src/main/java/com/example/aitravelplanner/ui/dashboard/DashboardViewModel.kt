package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.CardTravel

class DashboardViewModel : ViewModel() {
    private var _cardsList = MutableLiveData<ArrayList<CardTravel>>()
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList

    private var travelCardsList: ArrayList<CardTravel> = arrayListOf()
    private var usernames : ArrayList<String> = arrayListOf()
    private var userImages : ArrayList<String> = arrayListOf()
    private var travelImages : ArrayList<String> = arrayListOf()
    private var travelNames : ArrayList<String> = arrayListOf()
    private var travelAffinities : ArrayList<String> = arrayListOf()
    private var travelLikes : ArrayList<Int> = arrayListOf()
    private var timestamps : ArrayList<String> = arrayListOf()

    init{
        travelCardsList = arrayListOf()
        usernames = arrayListOf("Samuele", "Paolo", "Daniele", "Maria")
        userImages = arrayListOf("https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180")
        travelImages = arrayListOf("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg")
        travelNames = arrayListOf("Roma", "Roma", "Roma", "Roma")
        travelAffinities = arrayListOf("100", "100", "100", "100")
        travelLikes = arrayListOf(0, 0, 0, 0)
        timestamps = arrayListOf("12-10-2022", "12-10-2022", "12-10-2022", "12-10-2022")
    }

    fun getTravelCards(): ArrayList<CardTravel>{
        if(travelCardsList.isEmpty()) {
            for (i in (usernames.indices)) {
                val card = CardTravel(
                    username = usernames[i], userImage = userImages[i],
                    travelImage = travelImages[i], travelName = travelNames[i],
                    affinityPerc = travelAffinities[i], travelLikes = travelLikes[i],
                    timestamp = timestamps[i], isLiked = false
                )

                travelCardsList.add(card)
            }
        }
        return travelCardsList
    }

    fun isLiked(cardTravel: CardTravel): Boolean{
        cardTravel.isLiked = !cardTravel.isLiked
        if(cardTravel.isLiked)
            cardTravel.travelLikes!!.plus(1)
        else
            cardTravel.travelLikes!!.minus(1)

        _cardsList.notifyObserver()
        return cardTravel.isLiked
    }
    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value  = this.value
    }
}