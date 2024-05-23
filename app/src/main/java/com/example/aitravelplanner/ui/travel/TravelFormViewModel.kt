package com.example.aitravelplanner.ui.travel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.utils.OpenAIManager
import kotlinx.coroutines.launch

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

    private val openAIManager = OpenAIManager()

    fun confirmClicked() {
        if(sourceInput.value != "" && destinationInput.value != "" && days.value!!.toInt() > 0 && budget != ""){
            isFormCompleted.value = true

            val interests = user.value!!.interests

            val source = sourceInput.value ?: ""
            val isActualPosition = isActualPosition.value ?: false
            val destination = destinationInput.value ?: ""
            val isAutomaticDestination = isAutomaticDestination.value ?: false
            val days = days.value ?: "0"
            val isHotelChecked = isHotelChecked.value ?: false
            budget = determineBudget()

            val travelMap = mapOf(
                "Source" to source,
                //"Posizione attuale" to isActualPosition,
                "Destination" to if (isAutomaticDestination) "generate automatic destination" else destination,
                "Days" to days,
                "Hotel" to if(isHotelChecked) "Yes" else "No",
                "Budget" to budget
            )


            val travelPrompt = "Source: ${travelMap["Source"]}, " +
                                "Destination: ${travelMap["Destination"]}," +
                                "Days: ${travelMap["Days"]}," +
                                "Hotel: ${travelMap["Hotel"]}," +
                                "Budget: ${travelMap["Budget"]}," +
                                "ArtInterests: ${interests?.get("art")}/5," +
                                "EntertainmentInterests: ${interests?.get("entertainment")}/5," +
                                "NatureInterests: ${interests?.get("nature")}/5," +
                                "PartyInterests: ${interests?.get("party")}/5," +
                                "ShoppingInterests: ${interests?.get("shopping")}/5," +
                                "SportInterests: ${interests?.get("sport")}/5," +
                                "HistoryInterests: ${interests?.get("story")}/5,"

            viewModelScope.launch {
                openAIManager.preProcessTravel(travelPrompt)
            }
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