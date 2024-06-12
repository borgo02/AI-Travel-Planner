package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Classe astratta che estende BaseViewModel. Fornisce funzionalità comuni
 * per la gestione dei dati relativi ai viaggi, inclusi la lista delle card di viaggi, il viaggio selezionato
 * e le operazioni sui like.
 *
 * @constructor Viene iniettato un costruttore utilizzando l'annotazione @Inject
 */
abstract class TravelViewModel @Inject constructor() : BaseViewModel() {
    protected var _cardsList = MutableLiveData(arrayListOf<CardTravel>())
    protected val travelCardsSingleton = TravelCardsSingleton.getInstance()
    protected var _selectedTravel = MutableLiveData<CardTravel>()
    val selectedTravel: LiveData<CardTravel>
        get() = _selectedTravel

    /**
     * Metodo che deve essere implementato nelle classi concrete per impostare le card di viaggi.
     */
    protected abstract suspend fun setTravelCards()

    /**
     * Imposta il viaggio selezionato.
     */
    fun loadSelectedTravel(cardTravel: CardTravel) {
        _selectedTravel.value = cardTravel
    }

    /**
     * Metodo per gestire i like di una card di viaggio. Alterna lo stato del like
     * e aggiorna il numero di like del viaggio.
     */
    open fun isLiked(cardTravel: CardTravel): Boolean {
        cardTravel.isLiked = !cardTravel.isLiked
        if (cardTravel.isLiked)
            cardTravel.travelLikes = cardTravel.travelLikes!! + 1
        else
            cardTravel.travelLikes = cardTravel.travelLikes!! - 1

        // Esegue l'aggiornamento del like nel repository dell'utente.
        MainScope().launch {
            userRepository.updateLikedTravelByUser(currentUser.value!!.idUser, cardTravel.travelId, cardTravel.isLiked)
        }

        // Notifica le modifiche al singleton delle card di viaggi.
        travelCardsSingleton.notifyChanges()
        return cardTravel.isLiked
    }

    /**
     * Metodo astratto per gestire il click sul like. Deve essere implementato nelle classi concrete.
     */
    abstract fun clickLike()
}
