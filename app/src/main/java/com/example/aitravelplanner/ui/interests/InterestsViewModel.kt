package com.example.aitravelplanner.ui.interests

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InterestsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val storyValue = MutableLiveData(10.0f)
    val artValue = MutableLiveData(10.0f)
    val partyValue = MutableLiveData(10.0f)
    val natureValue = MutableLiveData(10.0f)
    val entertainmentValue = MutableLiveData(10.0f)
    val sportValue = MutableLiveData(10.0f)
    val shoppingValue = MutableLiveData(10.0f)

    fun confirmClicked() {
        // Il tuo codice da eseguire quando il bottone viene premuto
        var storV = storyValue.value
        println("Bottone premuto!")
    }
}