package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.launch

class ProfileViewModel : TravelViewModel() {
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _sharedTravelList = MutableLiveData(arrayListOf<CardTravel>())
    val sharedTravelList: LiveData<ArrayList<CardTravel>>
        get() = _sharedTravelList

    init{
        viewModelScope.launch {
            setUser(userRepository.getUserById("JoC41EXyP1LKpTviLoEQ")!!)
            setTravelCards(userRepository.getTravelsByUser(user.value!!.idUser!!))
        }
    }


    override suspend fun setTravelCards(travels: ArrayList<Travel>){
        for (travel in travels){
            val stageCardList = arrayListOf<StageCard>()
            for (stage in travel.stageList!!){
                stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))
            }
            val cardTravel = CardTravel(username = user.value!!.fullname!!, userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelName = travel.name!!, affinityPerc = "", travelLikes = travel.numberOfLikes, timestamp = travel.timestamp.toString(), isLiked = travel.isLiked!!, info = travel.info!!, stageCardList = stageCardList, userId = "JoC41EXyP1LKpTviLoEQ", travelId = travel.idTravel!! , isShared = travel.isShared!!)
            _cardsList.value?.add(cardTravel)
            if (travel.isShared == true)
                _sharedTravelList.value?.add(cardTravel)
            _sharedTravelList.notifyObserver()
            _cardsList.notifyObserver()
        }
    }

    fun shareTravel(cardTravel: CardTravel){
        cardTravel.isShared = true
        _sharedTravelList.value?.add(cardTravel)
        _sharedTravelList.notifyObserver()
        viewModelScope.launch {travelRepository.setTravelToShared(cardTravel.travelId)}
    }
}