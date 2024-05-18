package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import java.sql.Timestamp

class SharedTravelsViewModel : BaseViewModel()  {
    private var travelRepository: TravelRepository = TravelRepository()
    private var userRepository: UserRepository = UserRepository()

    private var _cardsList = MutableLiveData(arrayListOf<Travel>())
    val cardsList: LiveData<ArrayList<Travel>>
        get() = _cardsList

    init{
        setTravelCards()
    }

    private fun setTravelCards(){
        /*for (i in (usernamesId.indices)) {
            val card = Travel(idTravel=null, idUser=usernamesId[i],
                info=info[i], name=travelNames[i],
                isShared=false, timestamp=timestamps[i],
                numberOfLikes=travelLikes[i], imageUrl=travelImages[i],
                stageList=stage)

            _cardsList.value!!.add(card)
        }*/
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
}