package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel

class ProfileViewModel : ViewModel() {
    private var _cardsList = MutableLiveData(arrayListOf<CardTravel>())
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList

    private var usernames : ArrayList<String> = arrayListOf()
    private var userImages : ArrayList<String> = arrayListOf()
    private var travelImages : ArrayList<String> = arrayListOf()
    private var travelNames : ArrayList<String> = arrayListOf()
    private var timestamps : ArrayList<String> = arrayListOf()

    init{
        usernames = arrayListOf("Samuele", "Paolo", "Daniele", "Maria")
        userImages = arrayListOf("https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180", "https://tse2.mm.bing.net/th?id=OIP.bdhXRzn4dD3pumtLEqJsPQHaHa&pid=Api&P=0&h=180")
        travelImages = arrayListOf("https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg", "https://upload.wikimedia.org/wikipedia/commons/thumb/8/83/Colosseo_dal_Vittoriano%2C_Roma_I.jpg/1280px-Colosseo_dal_Vittoriano%2C_Roma_I.jpg")
        travelNames = arrayListOf("Roma", "Roma", "Roma", "Roma")
        timestamps = arrayListOf("12-10-2022", "12-10-2022", "12-10-2022", "12-10-2022")

        setTravelCards()
    }

    private fun setTravelCards(){
        for (i in (usernames.indices)) {
            val card = CardTravel(
                username = usernames[i], userImage = userImages[i],
                travelImage = travelImages[i], travelName = travelNames[i],
                affinityPerc = null, travelLikes = null,
                timestamp = timestamps[i], isLiked = false
            )

            _cardsList.value!!.add(card)
        }
    }

    fun isShared(): Boolean {
        return false
    }
}