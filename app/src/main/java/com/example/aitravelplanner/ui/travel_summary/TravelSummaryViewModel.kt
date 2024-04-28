package com.example.aitravelplanner.ui.travel_summary

import android.net.Uri
import androidx.collection.emptyLongSet
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.StageCard

class TravelSummaryViewModel: ViewModel() {
    private val _travelName = MutableLiveData<String>("")
    private val _stageSelectedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    private val _stageSearchedCardList = MutableLiveData<ArrayList<StageCard>>(arrayListOf<StageCard>())
    val searchText = MutableLiveData<String>("")

    val travelName: LiveData<String>
        get() = _travelName
    val stageSelectedCardList: LiveData<ArrayList<StageCard>>
        get() = _stageSelectedCardList
    val stageSearchedCardList: LiveData<ArrayList<StageCard>>
        get() = _stageSearchedCardList

    private var stageSearchedNameList: ArrayList<String> = arrayListOf()
    private var stageSearchedImageList: ArrayList<String> = arrayListOf()
    private var stageSearchedAffinityList: ArrayList<Int> = arrayListOf()
    private var stageSelectedNameList: ArrayList<String> = arrayListOf()
    private var stageSelectedImageList: ArrayList<String> = arrayListOf()
    private var stageSelectedAffinityList: ArrayList<Int> = arrayListOf()

    private fun getStageCards(stageCardList: ArrayList<StageCard>, stageNameList: ArrayList<String>, stageImageList: ArrayList<String>, stageAffinityList: ArrayList<Int>, isSelected: Boolean){
        for( i in (stageNameList.indices)){
            val stageCard = StageCard(stageName = stageNameList[i], stageImage = stageImageList[i], stageAffinity = stageAffinityList[i],isSearched = !(isSelected), isSelected = isSelected)
            stageCardList.add(stageCard)
        }
    }

    init{

        _travelName.value = "Roma"
        stageSelectedNameList.add("Colosseo")
        stageSelectedImageList.add("https://colosseo.it/sito/wp-content/uploads/2023/05/Colosseo_restauro_30-maggio_veduta-dallalto-scaled.jpg")
        stageSelectedAffinityList.add(100)

        stageSelectedNameList.add("San Pietro")
        stageSelectedImageList.add("https://upload.wikimedia.org/wikipedia/commons/thumb/f/f5/Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg/1200px-Basilica_di_San_Pietro_in_Vaticano_September_2015-1a.jpg")
        stageSelectedAffinityList.add(100)
        getStageCards(stageCardList = _stageSelectedCardList.value!!, stageNameList = stageSelectedNameList, stageImageList = stageSelectedImageList, stageAffinityList = stageSelectedAffinityList, isSelected = true)
        searchedClicked()
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
        if (searchText.value == "Colosseo") {
            _stageSearchedCardList.value = arrayListOf<StageCard>()
            stageSearchedNameList = arrayListOf<String>()
            stageSearchedImageList = arrayListOf<String>()
            stageSearchedAffinityList = arrayListOf<Int>()
            stageSearchedNameList.add("Colosseo")
            stageSearchedImageList.add("https://colosseo.it/sito/wp-content/uploads/2023/05/Colosseo_restauro_30-maggio_veduta-dallalto-scaled.jpg")
            stageSearchedAffinityList.add(100)

            getStageCards(
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

            getStageCards(
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

    fun savedClicked()
    {

    }

}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value  = this.value
}

