package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Questo View Model si occupa di gestire la logica legata al fragment del profilo e di chiamare metodi del repository per aggiornamento del database.
 *
 */
class ProfileViewModel @Inject constructor() : TravelViewModel() {
    val cardsList: LiveData<ArrayList<CardTravel>>
        get() = _cardsList
    private var _sharedTravelList = MutableLiveData(arrayListOf<CardTravel>())
    val sharedTravelList: LiveData<ArrayList<CardTravel>>
        get() = _sharedTravelList


    init{
        executeWithLoadingSuspend(block = {
            setTravelCards()
        })
    }

    /** Questa funzione imposta la lista di viaggi da visualizzare nel profilo utente.
     *
     * Viene osservata la lista travelCardsList della classe travelCardsSingleton che si occupa di gestire le
     * liste relative ai viaggi sia nella dashboard che ne profilo dell'utente
     */
    override suspend fun setTravelCards() {
        travelCardsSingleton.travelCardsList.observeForever { it ->
            val newSharedTravelList =arrayListOf<CardTravel>()
            val cardList = arrayListOf<CardTravel>()
            for (cardTravel in it) {
                if(cardTravel.userId == currentUser.value!!.idUser) {
                    cardList.add(cardTravel)
                    if(cardTravel.isShared)
                        newSharedTravelList.add(cardTravel)
                }
            }
            _cardsList.value = cardList
            _sharedTravelList.value = newSharedTravelList
        }
    }

    /** Questa funziona si occupa della condivisione di un viaggio da parte di un utente nel suo profilo.
     *
     * Viene richiamata quando l'utente clicca il bottone di "share" di una card di uno specifico viaggio
     */
    fun shareTravel(cardTravel: CardTravel){
        cardTravel.isShared = true
        MainScope().launch {
            travelRepository.setTravelToShared(cardTravel.travelId)
        }
        travelCardsSingleton.notifyChanges()
    }

    /** Questa funzione si occupa dell'aggiornamento del numero di like dei viaggi generati e condivisi da un utente specifico
     *
     */
    override fun clickLike(){
        super.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }
}