package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.repository.travel.ITravelRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.IUserRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class DashboardViewModel @Inject constructor(override val userRepository: IUserRepository = UserRepository.getInstance(), override val travelRepository: ITravelRepository = TravelRepository(), override val travelCardsSingleton: TravelCardsSingleton = TravelCardsSingleton.getInstance(), private val coroutineScopeProvider: CoroutineScope? = null) : TravelViewModel(userRepository,travelRepository, travelCardsSingleton, coroutineScopeProvider) {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        executeWithLoadingSuspend(block ={
            if(currentUser.value != null) {
                travelCardsSingleton.setTravelCards(currentUser.value!!.idUser)
                setTravelCards()
            }
        })
    }

    override suspend fun setTravelCards() {
        travelCardsSingleton.travelCardsList.observeForever { it ->
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