package com.example.aitravelplanner.ui.travel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.data.model.Travel
import javax.inject.Inject
import com.example.aitravelplanner.utils.OpenAIManager
import com.example.aitravelplanner.utils.ImagesManager
import kotlinx.coroutines.launch
import org.json.JSONObject

class TravelFormViewModel @Inject constructor() : BaseViewModel() {
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
    private val imagesManager = ImagesManager()

    fun confirmClicked() {
        if(sourceInput.value != "" && destinationInput.value != "" && days.value!!.toInt() > 0 && budget != ""){
            isFormCompleted.value = true

            val interests = currentUser.value!!.interests
            var json = JSONObject()
            var travels: ArrayList<Travel> = arrayListOf()
            val justVisitedCities: ArrayList<String> = arrayListOf()
            val places = arrayListOf<Any>()
            var stageImagesUrl: ArrayList<String> = arrayListOf()

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

            viewModelScope.launch {
                travels = userRepository.getTravelsByUser(currentUser.value!!.idUser)
            }

            for (travel in travels){
                justVisitedCities.add(travel.name!!)
            }

            val travelPrompt =  "Source: ${travelMap["Source"]}, " +
                    "Destination: ${travelMap["Destination"]}," +
                    "Days: ${travelMap["Days"]}," +
                    "Hotel: ${travelMap["Hotel"]}," +
                    "Budget: ${travelMap["Budget"]}," +
                    "ArtInterests: ${interests?.get("art")}/10," +
                    "EntertainmentInterests: ${interests?.get("entertainment")}/10," +
                    "NatureInterests: ${interests?.get("nature")}/10," +
                    "PartyInterests: ${interests?.get("party")}/10," +
                    "ShoppingInterests: ${interests?.get("shopping")}/10," +
                    "SportInterests: ${interests?.get("sport")}/10," +
                    "HistoryInterests: ${interests?.get("story")}/10," +
                    "Just visited cities: $justVisitedCities"

            /*val travelPrompt = "Source: Roma, " +
                    "Destination: generate automatic destination," +
                    "Days: 2," +
                    "Hotel: yes," +
                    "Budget: economico," +
                    "ArtInterests: 3/10," +
                    "EntertainmentInterests: 0/10," +
                    "NatureInterests: 1/10," +
                    "PartyInterests: 5/10," +
                    "ShoppingInterests: 5/10," +
                    "SportInterests: 0/10," +
                    "HistoryInterests: 1/10," +
                    "Just visited cities: [Milano, New York, Sidney, Manhattan]"*/

            viewModelScope.launch {
                json = openAIManager.preProcessTravel(travelPrompt)
                val placesArray = json.getJSONArray("Places to visit") ?: json.getJSONArray("Places to Visit")

                for (i in 0 until placesArray.length()) {
                    val placeObject = placesArray.getJSONObject(i)
                    val place = placeObject.get("Name")
                    places.add(place)
                }
                stageImagesUrl = imagesManager.getImages(places)
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