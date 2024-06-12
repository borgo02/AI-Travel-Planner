package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.ui.TravelViewModel
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.utils.notifyObserver
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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

    private var _logout = MutableLiveData<Boolean>(false)
    val logout: LiveData<Boolean>
        get() = _logout


    init{
        executeWithLoadingSuspend(block = {
            setTravelCards()
        })
    }

    /** Questo metodo imposta la lista di viaggi da visualizzare nel profilo utente.
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

    /**
     * Questo metodo si occupa della condivisione di un viaggio da parte di un utente nel suo profilo.
     * Viene richiamato quando l'utente clicca il bottone di "share" di una card di uno specifico viaggio
     */
    fun shareTravel(cardTravel: CardTravel){
        cardTravel.isShared = true
        MainScope().launch {
            travelRepository.setTravelToShared(cardTravel.travelId)
        }
        travelCardsSingleton.notifyChanges()
    }

    /**
     * Questo metodo si occupa dell'aggiornamento del numero di like dei viaggi generati e condivisi da un utente specifico
     */
    override fun clickLike(){
        super.isLiked(selectedTravel.value!!)
        _selectedTravel.notifyObserver()
    }

    /**
     * Metodo per la gestione del logout dall'applicazione
     */
    fun logout() {
        Firebase.auth.signOut()
        _logout.value = true
    }
}