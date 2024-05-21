package com.example.aitravelplanner.ui.travel

import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.BaseViewModel

class TravelFormViewModel : BaseViewModel() {
    private lateinit var budget: String
    var isFormCompleted = MutableLiveData<Boolean>(false)

    val sourceInput = MutableLiveData<String>("")
    val isActualPosition = MutableLiveData<Boolean>(false)
    val destinationInput = MutableLiveData<String>("")
    val isAutomaticDestination = MutableLiveData<Boolean>(false)
    val days = MutableLiveData<String>("0")
    val isHotelChecked = MutableLiveData<Boolean>(false)
    val isSmallBudget = MutableLiveData<Boolean>(false)
    val isMediumBudget = MutableLiveData<Boolean>(false)
    val isLargeBudget = MutableLiveData<Boolean>(false)

    fun confirmClicked() {
        if(sourceInput.value != "" && destinationInput.value != "" && days.value!!.toInt() > 0 && budget != ""){
            isFormCompleted.value = true

            val source = sourceInput.value ?: ""
            val isActualPosition = isActualPosition.value ?: false
            val destination = destinationInput.value ?: ""
            val isAutomaticDestination = isAutomaticDestination.value ?: false
            val days = days.value ?: "0"
            val isHotelChecked = isHotelChecked.value ?: false
            budget = determineBudget()

            val travelPreferences = mapOf(
                "Partenza" to source,
                "Posizione attuale" to isActualPosition,
                "Destinazione" to destination,
                "Destinazione automatica" to isAutomaticDestination,
                "Giorni" to days.toInt(),
                "Hotel" to isHotelChecked,
                "Budget" to budget
            )
        }
        else
            isFormCompleted.value = false
    }

    private fun determineBudget(): String {
        return when {
            isSmallBudget.value == true -> "Economico"
            isMediumBudget.value == true -> "Medio"
            isLargeBudget.value == true -> "Senza badare a spese"
            else -> ""
        }
    }
}