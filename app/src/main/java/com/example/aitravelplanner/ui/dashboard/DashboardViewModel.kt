package com.example.aitravelplanner.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import javax.inject.Inject

/** Questo View Model si occupa di gestire la logica legata al fragment della dashboard e di chiamare metodi del repository per aggiornamento del database.
 *
 */
class DashboardViewModel @Inject constructor() : TravelViewModel() {
    private var _searchedCardsList = MutableLiveData(arrayListOf<CardTravel>())
    val searchedCardsList: LiveData<ArrayList<CardTravel>>
        get() = _searchedCardsList

    val searchText = MutableLiveData<String>("")

    init{
        executeWithLoadingSuspend(block ={
            if (currentUser.value != null) {
                travelCardsSingleton.setTravelCards(currentUser.value!!.idUser)
                setTravelCards()
            }
        })
    }

    /** Questa funzione imposta la lista di viaggi da visualizzare nella dashboard.
     *
     * Viene osservata la lista travelCardsList della classe travelCardsSingleton che si occupa di gestire le
     * liste relative ai viaggi sia nella dashboard che ne profilo dell'utente
     */
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

    /** Questa funzione viene chiamata quando l'utente cerca una tappa da inserire nel viaggio generato
     *
     */
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

    /** Questa funzione viene chiamata quando l'utente clicca il bottone di like di uno specifico viaggio
     *
     */
    override fun clickLike(){
        super.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }
}