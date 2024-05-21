package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

abstract class TravelViewModel: BaseViewModel() {
    protected val travelRepository: TravelRepository = TravelRepository()
    protected var _cardsList = MutableLiveData(arrayListOf<CardTravel>())


    protected var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    protected abstract suspend fun setTravelCards(travels: ArrayList<Travel>)

    fun loadSelectedTravel(cardTravel: CardTravel){
        _selectedTravel.value = cardTravel
    }

    fun isLiked(cardTravel: CardTravel): Boolean{
        cardTravel.isLiked = !cardTravel.isLiked
        if(cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1

        MainScope().launch{
            userRepository.updateLikedTravelByUser(cardTravel.userId,cardTravel.travelId,cardTravel.isLiked)
        }

        return cardTravel.isLiked
    }

    fun clickLike(){
        this.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }

}