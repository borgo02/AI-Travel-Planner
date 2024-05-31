package com.example.aitravelplanner.ui.travel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import javax.inject.Inject
import com.example.aitravelplanner.utils.OpenAIManager
import com.example.aitravelplanner.utils.ImagesManager
import com.example.aitravelplanner.utils.notifyObserver
import com.google.firebase.Timestamp
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Date

class TravelFormViewModel @Inject constructor() : BaseViewModel() {
    private var budget: String = ""
    var hasJsonError = MutableLiveData<Boolean>(false)
    var isFormCompleted = MutableLiveData<Boolean>(false)
    val sourceInput = MutableLiveData<String>("")
    val isActualPosition = MutableLiveData<Boolean>(false)
    val destinationInput = MutableLiveData<String>("")
    val isAutomaticDestination = MutableLiveData<Boolean>(false)
    val days = MutableLiveData<String>("0")
    val isSmallBudget = MutableLiveData<Boolean>(false)
    val isMediumBudget = MutableLiveData<Boolean>(false)
    val isLargeBudget = MutableLiveData<Boolean>(false)

    private val openAIManager = OpenAIManager()
    private val imagesManager = ImagesManager()

    private val _travelName = MutableLiveData<String>("")
    private val _stageSelectedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    private val _stageSearchedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    val searchText = MutableLiveData<String>("")

    val travelName: LiveData<String>
        get() = _travelName

    val stageList:ArrayList<Stage> = arrayListOf()
    val stageSelectedCardList: LiveData<ArrayList<StageCard>>
        get() = _stageSelectedCardList
    val stageSearchedCardList: LiveData<ArrayList<StageCard>>
        get() = _stageSearchedCardList

    private var stageImagesUrl: ArrayList<String> = arrayListOf()
    private var stageSearchedNameList: ArrayList<String> = arrayListOf()
    private var stageSearchedImageList: ArrayList<String> = arrayListOf()
    private var stageSearchedAffinityList: ArrayList<Int> = arrayListOf()
    private var stageSelectedNameList: ArrayList<String> = arrayListOf()
    private var stageSelectedImageList: ArrayList<String> = arrayListOf()
    private var stageSelectedAffinityList: ArrayList<Int> = arrayListOf()
    private val stageDescriptions = arrayListOf<String>()
    private var description: String =""

    fun confirmClicked() {
        budget = determineBudget()
        if((sourceInput.value != "" || isActualPosition.value == true) && (destinationInput.value != "" || isAutomaticDestination.value == true) && days.value != "" && days.value!!.toInt() > 0 && budget != ""){
            isFormCompleted.value = true
            val interests = currentUser.value!!.interests as Map<String,Float>
            var json = JSONObject()
            val justVisitedCities: ArrayList<String> = arrayListOf()

            val source = sourceInput.value ?: ""
            val isActualPosition = isActualPosition.value ?: false
            val destination = destinationInput.value ?: ""
            val isAutomaticDestination = isAutomaticDestination.value ?: false
            val days = days.value!!.toInt().toString()


            val travelMap = mapOf(
                "Source" to source,
                //"Posizione attuale" to isActualPosition,
                "Destination" to if (isAutomaticDestination) "generate automatic destination" else destination,
                "Days" to days,
                "Budget" to budget
            )

            viewModelScope.launch {
                for (travel in userRepository.getTravelsByUser(currentUser.value!!.idUser))
                    justVisitedCities.add(travel.name!!)

                val travelPrompt = "Source: ${travelMap["Source"]}, " +
                        "Destination: ${travelMap["Destination"]}," +
                        "Days: ${travelMap["Days"]}," +
                        "Budget: ${travelMap["Budget"]}," +
                        "ArtInterests: ${interests["art"]}," +
                        "EntertainmentInterests: ${interests["entertainment"]}," +
                        "NatureInterests: ${interests["nature"]}," +
                        "PartyInterests: ${interests["party"]}," +
                        "ShoppingInterests: ${interests["shopping"]}," +
                        "SportInterests: ${interests["sport"]}," +
                        "HistoryInterests: ${interests["story"]}," +
                        "Just visited cities: $justVisitedCities"

                json = openAIManager.preProcessTravel(travelPrompt)
                if (!json.has("error")) {
                    hasJsonError.value = false
                    val placesArray =
                        json.getJSONArray("Places to visit") ?: json.getJSONArray("Places to Visit")

                    _travelName.value = json.getString("City to visit")
                    description += json.getString("Description")
                    description += json.getString("Itinerary")
                    var names = arrayListOf<String>()
                    names.add(_travelName.value!!)

                    for (i in 0 until placesArray.length()) {
                        val placeObject = placesArray.getJSONObject(i)
                        val place = placeObject.getString("Name")
                        val description = placeObject.getString("Description")
                        stageDescriptions.add(description)
                        names.add(place)
                        stageSelectedNameList.add(place)
                    }

                    stageImagesUrl = imagesManager.getImages(names)

                    for (stageDescription in stageDescriptions)
                        description += stageDescription

                    for (i in 0 until stageImagesUrl.size) {
                        if (i == 0) {
                            stageSelectedAffinityList.add(100)
                            continue
                        }
                        stageSelectedAffinityList.add(100)
                        stageSelectedImageList.add(stageImagesUrl[i])
                    }

                    /*for(i in 0 until stageImagesUrl.size)
                    stageSelectedAffinityList.add(100)*/

                    setStageCards(
                        stageCardList = _stageSelectedCardList.value!!,
                        stageNameList = stageSelectedNameList,
                        stageImageList = stageSelectedImageList,
                        stageAffinityList = stageSelectedAffinityList,
                        isSelected = true
                    )
                }
                else
                    hasJsonError.value = true
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

    private fun setStageCards(stageCardList: ArrayList<StageCard>, stageNameList: ArrayList<String>, stageImageList: ArrayList<String>, stageAffinityList: ArrayList<Int>, isSelected: Boolean){
        for( i in (stageNameList.indices)){
            val stage = Stage(idStage = null, name = stageNameList[i], imageUrl = stageImageList[i], city= travelName.value!!, description= stageDescriptions[i], position = i+1)
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i],isSearched = !(isSelected), isSelected = isSelected)
            stageCardList.add(stageCard)
            stageList.add(stage)
        }
        _stageSelectedCardList.notifyObserver()
    }

    fun deleteStage(stageCard: StageCard){
        _stageSelectedCardList.value!!.remove(stageCard)
        _stageSelectedCardList.notifyObserver()
    }

    fun addStage(stageCard: StageCard){
        _stageSearchedCardList.value!!.remove(stageCard)
        stageCard.isSearched = false
        stageCard.isSelected = true
        _stageSelectedCardList.value!!.add(stageCard)
        _stageSelectedCardList.notifyObserver()
    }

    fun searchedClicked(){
        if(searchText.value == "Colosseo") {
            _stageSearchedCardList.value = arrayListOf<StageCard>()
            stageSearchedNameList = arrayListOf<String>()
            stageSearchedImageList = arrayListOf<String>()
            stageSearchedAffinityList = arrayListOf<Int>()
            stageSearchedNameList.add("Colosseo")
            stageSearchedImageList.add("https://colosseo.it/sito/wp-content/uploads/2023/05/Colosseo_restauro_30-maggio_veduta-dallalto-scaled.jpg")
            stageSearchedAffinityList.add(100)

            setStageCards(
                stageCardList = _stageSearchedCardList.value!!,
                stageNameList = stageSearchedNameList,
                stageImageList = stageSearchedImageList,
                stageAffinityList = stageSearchedAffinityList,
                isSelected = false
            )
        }
        else if(searchText.value == "San Pietro"){
            _stageSearchedCardList.value = arrayListOf<StageCard>()
            stageSearchedNameList = arrayListOf<String>()
            stageSearchedImageList = arrayListOf<String>()
            stageSearchedAffinityList = arrayListOf<Int>()
            stageSearchedNameList.add("San Pietro")
            stageSearchedImageList.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg/1200px-Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg")
            stageSearchedAffinityList.add(100)

            setStageCards(
                stageCardList = _stageSearchedCardList.value!!,
                stageNameList = stageSearchedNameList,
                stageImageList = stageSearchedImageList,
                stageAffinityList = stageSearchedAffinityList,
                isSelected = false
            )
        }
        else {
            _stageSearchedCardList.value = arrayListOf<StageCard>()
        }
    }

    fun savedClicked(){
        val travel = Travel(idTravel = null, idUser = currentUser.value!!.idUser, info = description, name = travelName.value, isShared = false, timestamp = Timestamp.now().toDate(), numberOfLikes = 0, imageUrl = stageImagesUrl[0], stageList = stageList, isLiked = false)
        viewModelScope.launch { travelRepository.setTravel(travel) }
    }
}