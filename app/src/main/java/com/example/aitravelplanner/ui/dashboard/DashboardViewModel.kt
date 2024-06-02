package com.example.aitravelplanner.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import javax.inject.Inject

class DashboardViewModel @Inject constructor() : TravelViewModel() {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        if (currentUser.value != null) {
            travelRepository.travelsCollectionReference.addSnapshotListener { newValue, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (newValue != null) {
                    _searchedCardsList.value?.clear()
                    _cardsList.value?.clear()
                    executeWithLoadingSuspend(block = {
                        setTravelCards(travelRepository.getSharedTravels(currentUser.value!!.idUser))
                    })
                }
            }
        }
    }

    override suspend fun setTravelCards(travels: ArrayList<Travel>){
        for (travel in travels){
            val userTravel: User = travel.idUser?.path?.let { userRepository.getUserById(it.substringAfterLast("/")) }!!
            val stageCardList = arrayListOf<StageCard>()
            for (stage in travel.stageList!!)
                stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

            _cardsList.value?.add(CardTravel(username = userTravel.fullname, userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png", travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelName = travel.name!!, affinityPerc = "", travelLikes = travel.numberOfLikes, timestamp = travel.timestamp.toString(), isLiked = travel.isLiked!!, info = travel.info!!, stageCardList = stageCardList, userId = userTravel.idUser, travelId = travel.idTravel!! ))
        }
        _searchedCardsList.value!!.addAll(_cardsList.value!!)
        _searchedCardsList.notifyObserver()
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
}