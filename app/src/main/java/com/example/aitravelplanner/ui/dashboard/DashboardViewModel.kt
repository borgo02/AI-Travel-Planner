package com.example.aitravelplanner.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.EventBus
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DashboardViewModel @Inject constructor() : TravelViewModel() {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        executeWithLoadingSuspend(block = {
            if (currentUser.value != null) {
                setTravelCards(travelRepository.getSharedTravels(currentUser.value!!.idUser))
            }
        })

        EventBus.profileDataChanged.observeForever{hasChanged ->
            executeWithLoadingSuspend(block = {
                if(hasChanged) {
                    _searchedCardsList.value?.clear()
                    _cardsList.value?.clear()
                    setTravelCards(travelRepository.getSharedTravels(currentUser.value!!.idUser))
                    EventBus.resetProfileDataChanged()
                }
            })
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
        Log.d("Ciao", "Card List: ${_searchedCardsList.value!!}")
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