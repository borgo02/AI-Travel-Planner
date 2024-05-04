package com.example.aitravelplanner.ui.travel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelFormViewModel : ViewModel() {
    private lateinit var budget: String

    private val _sourceInput = MutableLiveData<String>("")
    private val _isActualPosition = MutableLiveData<Boolean>(false)
    private val _destinationInput = MutableLiveData<String>("")
    private val _isAutomaticDestination = MutableLiveData<Boolean>(false)
    private val _days = MutableLiveData<String>("0")
    private val _isHotelChecked = MutableLiveData<Boolean>(false)
    private val _isSmallBudget = MutableLiveData<Boolean>(false)
    private val _isMediumBudget = MutableLiveData<Boolean>(false)
    private val _isLargeBudget = MutableLiveData<Boolean>(false)

    val sourceInput: LiveData<String>
        get()= _sourceInput
    val isActualPosition: LiveData<Boolean>
        get()= _isActualPosition
    val destinationInput: LiveData<String>
        get()= _destinationInput
    val isAutomaticDestination: LiveData<Boolean>
        get()= _isAutomaticDestination
    val days: LiveData<String>
        get()= _days
    val isHotelChecked: LiveData<Boolean>
        get()= _isHotelChecked
    val isSmallBudget: LiveData<Boolean>
        get()= _isSmallBudget
    val isMediumBudget: LiveData<Boolean>
        get()= _isMediumBudget
    val isLargeBudget: LiveData<Boolean>
        get()= _isLargeBudget

    fun confirmClicked() {
        budget =    if(_isSmallBudget.value == true)
                        "Umile"
                    else{
                        if(_isMediumBudget.value == true)
                            "Medio"
                        else
                            "Alto"
                    }

        var travelPreferences = mapOf(
            "Partenza" to _sourceInput.value,
            "Posizione attuale" to _isActualPosition.value,
            "Destinazione" to _destinationInput.value,
            "Destinazione automatica" to _isAutomaticDestination.value,
            "Giorni" to _days.value,
            "Hotel" to _isHotelChecked.value,
            "Budget" to budget
        )

        Log.d("Ciao", "$travelPreferences")
        //chiamata al service per salvare nel db
    }
}