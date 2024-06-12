package com.example.aitravelplanner.ui.travel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.ui.BaseViewModel
import com.example.aitravelplanner.data.model.Stage
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.components.stageCard.StageCard
import javax.inject.Inject
import com.example.aitravelplanner.utils.OpenAIManager
import com.example.aitravelplanner.utils.ImagesManager
import com.example.aitravelplanner.utils.notifyObserver
import com.google.firebase.Timestamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class TravelFormViewModel @Inject constructor(override val userRepository: UserRepository = UserRepository.getInstance(), override val travelRepository: TravelRepository = TravelRepository(), private val coroutineScopeProvider: CoroutineScope? = null) : BaseViewModel(userRepository, travelRepository, coroutineScopeProvider) {
    private var budget: String = ""
    var hasJsonError = MutableLiveData<Boolean>(false)
    var isFormEmpty = MutableLiveData<Boolean>(false)
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
    private var stageSearchedDescriptions = arrayListOf<String>()
    private var stageSelectedNameList: ArrayList<String> = arrayListOf()
    private var stageSelectedImageList: ArrayList<String> = arrayListOf()
    private var stageSelectedAffinityList: ArrayList<Int> = arrayListOf()
    private var stageSelectedDescriptions = arrayListOf<String>()
    private var description: String = ""

    fun confirmClicked() {
        hasJsonError.value = false
        budget = determineBudget()
        if((sourceInput.value != "" || isActualPosition.value == true) && (destinationInput.value != "" || isAutomaticDestination.value == true) && days.value != "" && days.value!!.toInt() > 0 && budget != ""){
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

            executeWithLoadingSuspend(block = {
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

                json = if(travelMap["Destination"] == "generate automatic destination")
                    openAIManager.preProcessTravel(travelPrompt, true)
                else
                    openAIManager.preProcessTravel(travelPrompt, false)

                if (!json.has("error")) {
                    hasJsonError.value = false
                    val placesArray = json.getJSONArray("Places to visit") ?: json.getJSONArray("Places to Visit")

                    _travelName.value = json.getString("City to visit")
                    description += json.getString("Description") + "\n"
                    val names = arrayListOf<String>()
                    names.add(_travelName.value!!)
                    for (i in 0 until placesArray.length()) {
                        val placeObject = placesArray.getJSONObject(i)
                        val place = placeObject.getString("Place")
                        val description = placeObject.getString("Description")
                        stageSelectedDescriptions.add(description)
                        names.add(place)
                        stageSelectedNameList.add(place)
                    }

                    stageImagesUrl = imagesManager.getImages(names)


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
                        isSelected = true,
                        descriptionList = stageSelectedDescriptions
                    )
                    _stageSelectedCardList.notifyObserver()
                    navigate(TravelFormFragmentDirections.actionTravelFormFragmentToTravelSummaryFragment())
                }
                else
                    hasJsonError.value = true
            })
        }
        else
            isFormEmpty.value = true
    }


    private fun determineBudget(): String {
        return when {
            isSmallBudget.value == true -> "Economico"
            isMediumBudget.value == true -> "Medio"
            isLargeBudget.value == true -> "Senza badare a spese"
            else -> ""
        }
    }

    private fun setStageCards(stageCardList: ArrayList<StageCard>, stageNameList: ArrayList<String>, stageImageList: ArrayList<String>, stageAffinityList: ArrayList<Int>, isSelected: Boolean, descriptionList: ArrayList<String>){
        for( i in (stageNameList.indices)){
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i],isSearched = !(isSelected), isSelected = isSelected, description = descriptionList[i])
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
            stageSearchedDescriptions = arrayListOf<String>()

            for(stage in filteredStages){
                if(stage.name !in _stageSelectedCardList.value!!.map { it.stageName }) {
                    stageSearchedNameList.add(stage.name)
                    stageSearchedImageList.add(stage.imageUrl)
                    stageSearchedAffinityList.add(100)
                    stageSearchedDescriptions.add(stage.description)
                }
            }

            setStageCards(
                stageCardList = _stageSearchedCardList.value!!,
                stageNameList = stageSearchedNameList,
                stageImageList = stageSearchedImageList,
                stageAffinityList = stageSearchedAffinityList,
                isSelected = false,
                descriptionList = stageSearchedDescriptions

            )
            _stageSearchedCardList.notifyObserver()
        }
    }

    fun savedClicked() {
        executeWithLoadingSuspend(block = {
            var i = 1
            for (stagCard in stageSelectedCardList.value!!) {
                val stage = Stage(
                    idStage = null,
                    name = stagCard.stageName,
                    imageUrl = stagCard.stageImage,
                    city = travelName.value!!,
                    description = stagCard.description,
                    position = i
                )
                stageList.add(stage)
                i += 1
            }
            for (stage in stageList){
                description += stage.name + ": "
                description += stage.description + "\n"
            }

            val userRef = userRepository.getUserReference(currentUser.value!!.idUser)
            val travel = Travel(idTravel = null, idUser = userRef, info = description, name = travelName.value, isShared = false, timestamp = Timestamp.now().toDate(), numberOfLikes = 0, imageUrl = stageImagesUrl[0], stageList = stageList, isLiked = false)
            travelRepository.setTravel(travel)
            isTravelCreated.value = true
            TravelCardsSingleton.getInstance().addTravel(travel, currentUser.value!!)
            clearViewModel()
        })
    }

    fun clearViewModel(){
        isTravelCreated.value = false
        isFormEmpty.value = false
        budget = ""
        hasJsonError.value = false
        isFormEmpty.value = false
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
        stageSelectedDescriptions = arrayListOf<String>()
        description = ""
    }
}

