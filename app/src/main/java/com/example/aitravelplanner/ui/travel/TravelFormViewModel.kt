package com.example.aitravelplanner.ui.travel

import android.util.Log
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
    val isTravelCreated = MutableLiveData<Boolean>(false)

    private val openAIManager = OpenAIManager()
    private val imagesManager = ImagesManager()

    private val _travelName = MutableLiveData<String>("")
    private val _stageSelectedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    private val _stageSearchedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    val searchText = MutableLiveData<String>("")

    val travelName: LiveData<String>
        get() = _travelName

    var stageList:ArrayList<Stage> = arrayListOf()
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
    private var stageDescriptions = arrayListOf<String>()
    private var description: String =""

    fun confirmClicked() {
        hasJsonError.value = false
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

            Log.d("Debug", travelMap["Destination"].toString())

            viewModelScope.launch {
                for (travel in userRepository.getTravelsByUser(currentUser.value!!.idUser)) {
                    justVisitedCities.add(travel.name!!)
                }

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
                    val placesArray = json.getJSONArray("Places to visit") ?: json.getJSONArray("Places to Visit")

                    _travelName.value = json.getString("City to visit")
                    description += json.getString("Description")
                    description += json.getString("Itinerary")
                    val names = arrayListOf<String>()
                    names.add(_travelName.value!!)
                    for (i in 0 until placesArray.length()) {
                        val placeObject = placesArray.getJSONObject(i)
                        val place = placeObject.getString("Place")
                        val description = placeObject.getString("Description")
                        stageDescriptions.add(description)
                        names.add(place)
                        stageSelectedNameList.add(place)
                    }

                    stageImagesUrl = imagesManager.getImages(names)

                    for (stageDescription in stageDescriptions)
                        description += stageDescription

                    for (i in 0 until stageImagesUrl.size) {
                        if(i == 0){
                            stageSelectedAffinityList.add(100)
                            continue
                        }
                        stageSelectedAffinityList.add(100)
                        stageSelectedImageList.add(stageImagesUrl[i])
                    }

                    setStageCards(
                        stageCardList = _stageSelectedCardList.value!!,
                        stageNameList = stageSelectedNameList,
                        stageImageList = stageSelectedImageList,
                        stageAffinityList = stageSelectedAffinityList,
                        isSelected = true
                    )
                    _stageSelectedCardList.notifyObserver()
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
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i],isSearched = !(isSelected), isSelected = isSelected)
            stageCardList.add(stageCard)
        }
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
        viewModelScope.launch{
            val filteredStages: ArrayList<Stage> = travelRepository.getFilteredStagesByCity(searchText.value!!, travelName.value!!)
            _stageSearchedCardList.value = arrayListOf<StageCard>()
            stageSearchedNameList = arrayListOf<String>()
            stageSearchedImageList = arrayListOf<String>()
            stageSearchedAffinityList = arrayListOf<Int>()

            for(stage in filteredStages){
                stageSearchedNameList.add(stage.name)
                stageSearchedImageList.add(stage.imageUrl)
                stageSearchedAffinityList.add(100)
            }

            setStageCards(
                stageCardList = _stageSearchedCardList.value!!,
                stageNameList = stageSearchedNameList,
                stageImageList = stageSearchedImageList,
                stageAffinityList = stageSearchedAffinityList,
                isSelected = false
            )
            _stageSearchedCardList.notifyObserver()
        }
    }

    fun savedClicked() {
        viewModelScope.launch {
            var i = 1
            for(stagCard in stageSelectedCardList.value!!){
                val stage = Stage(idStage = null, name = stagCard.stageName, imageUrl = stagCard.stageImage, city= travelName.value!!, description= stagCard.stageName, position = i)
                stageList.add(stage)
                i +=1
            }

            val userRef = userRepository.getUserReference(currentUser.value!!.idUser)
            val travel = Travel(idTravel = null, idUser = userRef, info = description, name = travelName.value, isShared = false, timestamp = Timestamp.now().toDate(), numberOfLikes = 0, imageUrl = stageImagesUrl[0], stageList = stageList, isLiked = false)
            travelRepository.setTravel(travel)
            isTravelCreated.value = true
            isTravelCreated.value = false
            isFormCompleted.value = false
            budget = ""
            hasJsonError.value = false
            isFormCompleted.value = false
            sourceInput.value = ""
            isActualPosition.value = false
            destinationInput.value =""
            isAutomaticDestination.value = false
            days.value = "0"
            isSmallBudget.value = false
            isMediumBudget.value = false
            isLargeBudget.value = false
            isTravelCreated.value = false

            _travelName.value = ""
            _stageSelectedCardList.value = arrayListOf<StageCard>()
            _stageSearchedCardList.value = arrayListOf<StageCard>()
            searchText.value = ""



            stageList = arrayListOf()
            stageImagesUrl = arrayListOf()
            stageSearchedNameList = arrayListOf()
            stageSearchedImageList = arrayListOf()
            stageSearchedAffinityList = arrayListOf()
            stageSelectedNameList = arrayListOf()
            stageSelectedImageList = arrayListOf()
            stageSelectedAffinityList = arrayListOf()
            stageDescriptions = arrayListOf<String>()
            description = ""
        }
    }
}