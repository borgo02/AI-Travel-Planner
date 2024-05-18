package com.example.aitravelplanner.ui.dashboard
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val travelRepository: TravelRepository = TravelRepository()
    private val userRepository: UserRepository = UserRepository()
    private var _cardsList = MutableLiveData(arrayListOf<CardTravel>())
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    private var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    val searchText = MutableLiveData<String>("")
    private var searchJob: Job? = null

    init{
        viewModelScope.launch {
            setTravelCards(travelRepository.getTravels())
            _searchedCardsList.value =  _searchedCardsList.value
        }
    }

    private suspend fun setTravelCards(travels: ArrayList<Travel>){
        for (travel in travels){
            var user: User = userRepository.getUserByTravel(travel.idTravel!!)!!
            _cardsList.value?.add(CardTravel(username = user.fullname!!, userImage = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelImage = travel.imageUrl ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnfAxGV-fZxGL9elM_hQ2tp7skLeSwMyUiwo4lMm1zyA&s", travelName = travel.name!!, affinityPerc = "", travelLikes = travel.numberOfLikes, timestamp = travel.timestamp.toString(), isLiked = travel.isLiked!!, info = travel.info!!))
        }
        _searchedCardsList.value!!.addAll(_cardsList.value!!)
    }

    fun isLiked(cardTravel: CardTravel): Boolean{
        cardTravel.isLiked = !cardTravel.isLiked
        if(cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1
        return cardTravel.isLiked
    }

    fun search(){
        _searchedCardsList.value!!.clear()

        for(card in _cardsList.value!!) {
            if (searchText.value.toString().lowercase() in card.travelName!!.lowercase())
                _searchedCardsList.value!!.add(card)
            _searchedCardsList.value = _searchedCardsList.value
        }
    }

    fun loadSelectedTravel(cardTravel: CardTravel){
        _selectedTravel.value = cardTravel
    }

}