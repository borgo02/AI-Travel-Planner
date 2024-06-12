package com.example.aitravelplanner.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.repository.travel.ITravelRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.IUserRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.BaseViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class TravelViewModel@Inject constructor(override val userRepository: IUserRepository = UserRepository.getInstance(), override val travelRepository: ITravelRepository = TravelRepository(),
                                                  open val travelCardsSingleton: TravelCardsSingleton = TravelCardsSingleton.getInstance()) : BaseViewModel(userRepository,travelRepository) {
    protected var _cardsList = MutableLiveData(arrayListOf<CardTravel>())


    protected var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    protected abstract suspend fun setTravelCards()

    fun loadSelectedTravel(cardTravel: CardTravel){
        _selectedTravel.value = cardTravel
    }

    open fun isLiked(cardTravel: CardTravel): Boolean {
        cardTravel.isLiked = !cardTravel.isLiked
        if (cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1

        MainScope().launch {
            userRepository.updateLikedTravelByUser(currentUser.value!!.idUser, cardTravel.travelId, cardTravel.isLiked)
        }

        travelCardsSingleton.notifyChanges()
        return cardTravel.isLiked
    }

    abstract fun clickLike()
}