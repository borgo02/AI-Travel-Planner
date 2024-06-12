package com.example.aitravelplanner.ui.interests

import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.BaseViewModel
import javax.inject.Inject

/**
 * ViewModel che gestisce la logica per la visualizzazione degli interessi dell'utente.
 *
 * @property userRepository Repository per la gestione degli utenti.
 */
class InterestsViewModel @Inject constructor() : BaseViewModel() {
    val storyValue = MutableLiveData(5.0f)
    val artValue = MutableLiveData(5.0f)
    val partyValue = MutableLiveData(5.0f)
    val natureValue = MutableLiveData(5.0f)
    val entertainmentValue = MutableLiveData(5.0f)
    val sportValue = MutableLiveData(5.0f)
    val shoppingValue = MutableLiveData(5.0f)

    /**
     * Metodo chiamato quando viene confermata la selezione degli interessi.
     * Aggiorna gli interessi dell'utente nel repository.
     */
    fun confirmClicked() {
        executeWithLoading(block = {
            val interestEntity = mapOf("story" to storyValue.value!!,
                "art" to artValue.value!!,
                "party" to partyValue.value!!,
                "nature" to natureValue.value!!,
                "entertainment" to entertainmentValue.value!!,
                "sport" to sportValue.value!!,
                "shopping" to shoppingValue.value!!)

            currentUser.value!!.interests = interestEntity
            currentUser.value!!.isInitialized = true
            userRepository.updateUser(currentUser.value!!)
            navigateBack()
        })
    }
}