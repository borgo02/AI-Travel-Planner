package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.EventBus
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
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
                    _sharedTravelList.value?.clear()
                    _cardsList.value?.clear()
                    setTravelCards(userRepository.getTravelsByUser(currentUser.value!!.idUser))
                    EventBus.resetDashboardDataChanged()
                }
            })
        }
    }

    override suspend fun setTravelCards(travels: ArrayList<Travel>){
        for (travel in travels){
            val stageCardList = arrayListOf<StageCard>()
            for (stage in travel.stageList!!)
                stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

            val cardTravel = CardTravel(username = currentUser.value!!.fullname, userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png", travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelName = travel.name!!, affinityPerc = "", travelLikes = travel.numberOfLikes, timestamp = travel.timestamp.toString(), isLiked = travel.isLiked!!, info = travel.info!!, stageCardList = stageCardList, userId = "JoC41EXyP1LKpTviLoEQ", travelId = travel.idTravel!! , isShared = travel.isShared!!)
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

        viewModelScope.launch {
            travelRepository.setTravelToShared(cardTravel.travelId)
        }
    }

    public override fun isLiked(cardTravel: CardTravel, vmReference: String): Boolean{
        if(vmReference == "dashboard")
            EventBus.notifyDashboardDataChanged()
        else if(vmReference == "profile")
            EventBus.notifyProfileDataChanged()

        cardTravel.isLiked = !cardTravel.isLiked
        if(cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1

        MainScope().launch {
            userRepository.updateLikedTravelByUser(currentUser.value!!.idUser,cardTravel.travelId,cardTravel.isLiked)
        }

        return cardTravel.isLiked
    }

    override fun clickLike(){
        this.isLiked(selectedTravel.value!!, "dashboard")
        _selectedTravel.notifyObserver()
    }
}