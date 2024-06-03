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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardViewModel @Inject constructor() : TravelViewModel() {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        if (currentUser.value != null)
            executeWithLoadingSuspend(block = {
                setTravelCards(travelRepository.getSharedTravels(currentUser.value!!.idUser))
            })

        EventBus.profileDataChanged.observeForever{hasChanged ->
            if(hasChanged) {
                executeWithLoadingSuspend(block = {
                    Log.d("DahsboardViewModel", "Chiamiamo il metodo setTravels()")
                    setTravelCards(travelRepository.getSharedTravels(currentUser.value!!.idUser))
                    EventBus.resetProfileDataChanged()
                })
            }
        }
    }

    override suspend fun setTravelCards(travels: ArrayList<Travel>) {
        withContext(Dispatchers.Main) {
            val newCardsList = arrayListOf<CardTravel>()
            val newSearchedCardsList = arrayListOf<CardTravel>()

            for (travel in travels) {
                val userTravel: User = travel.idUser?.path?.let {
                    userRepository.getUserById(it.substringAfterLast("/"))
                }!!

                val stageCardList = arrayListOf<StageCard>()
                for (stage in travel.stageList!!)
                    stageCardList.add(StageCard(stageName = stage.name, stageImage = stage.imageUrl, stageAffinity = 11))

                val cardTravel = CardTravel(
                    username = userTravel.fullname,
                    userImage = "https://cdn-icons-png.flaticon.com/512/8847/8847419.png",
                    travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s",
                    travelName = travel.name!!,
                    affinityPerc = "",
                    travelLikes = travel.numberOfLikes,
                    timestamp = travel.timestamp.toString(),
                    isLiked = travel.isLiked!!,
                    info = travel.info!!,
                    stageCardList = stageCardList,
                    userId = userTravel.idUser,
                    travelId = travel.idTravel!!
                )

                newCardsList.add(cardTravel)
                newSearchedCardsList.add(cardTravel)
            }

            _cardsList.value = newCardsList
            _searchedCardsList.value = newSearchedCardsList
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
        super.isLiked(selectedTravel.value!!, "dashboard")
        _selectedTravel.notifyObserver()
    }
}