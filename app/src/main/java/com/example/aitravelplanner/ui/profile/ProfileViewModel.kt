package com.example.aitravelplanner.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.EventBus
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor() : TravelViewModel() {
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _sharedTravelList = MutableLiveData(arrayListOf<CardTravel>())
    val sharedTravelList: LiveData<ArrayList<CardTravel>>
        get() = _sharedTravelList

    init{
        executeWithLoadingSuspend(block = {
            setTravelCards(userRepository.getTravelsByUser(currentUser.value!!.idUser))
        })

        EventBus.dashboardDataChanged.observeForever{hasChanged ->
            executeWithLoadingSuspend(block = {
                if(hasChanged) {
                    setTravelCards(userRepository.getTravelsByUser(currentUser.value!!.idUser))
                    EventBus.resetDashboardDataChanged()
                }
            })
        }
    }

    override suspend fun setTravelCards(travels: ArrayList<Travel>) {
        withContext(Dispatchers.Main) {
            val newCardsList = arrayListOf<CardTravel>()
            val newSharedTravelList = arrayListOf<CardTravel>()

            for (travel in travels) {
                val stageCardList = arrayListOf<StageCard>()
                for (stage in travel.stageList!!)
                    stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

                val cardTravel = CardTravel(
                    username = currentUser.value!!.fullname,
                    userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png",
                    travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s",
                    travelName = travel.name!!,
                    affinityPerc = "",
                    travelLikes = travel.numberOfLikes,
                    timestamp = travel.timestamp.toString(),
                    isLiked = travel.isLiked!!,
                    info = travel.info!!,
                    stageCardList = stageCardList,
                    userId = currentUser.value!!.idUser,
                    travelId = travel.idTravel!!,
                    isShared = travel.isShared!!
                )

                newCardsList.add(cardTravel)
                if (travel.isShared == true)
                    newSharedTravelList.add(cardTravel)
            }

            _cardsList.value = newCardsList
            _sharedTravelList.value = newSharedTravelList
        }
    }


    fun shareTravel(cardTravel: CardTravel){
        cardTravel.isShared = true
        _sharedTravelList.value?.add(cardTravel)
        _sharedTravelList.notifyObserver()

        viewModelScope.launch {
            travelRepository.setTravelToShared(cardTravel.travelId)
        }
    }

    override fun clickLike(){
        super.isLiked(selectedTravel.value!!, "profile")
        _selectedTravel.notifyObserver()
    }
}