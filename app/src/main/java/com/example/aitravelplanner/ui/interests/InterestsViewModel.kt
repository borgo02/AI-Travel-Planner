package com.example.aitravelplanner.ui.interests

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.R
import kotlinx.coroutines.launch
import javax.inject.Inject

class InterestsViewModel @Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val storyValue = MutableLiveData(5.0f)
    val artValue = MutableLiveData(5.0f)
    val partyValue = MutableLiveData(5.0f)
    val natureValue = MutableLiveData(5.0f)
    val entertainmentValue = MutableLiveData(5.0f)
    val sportValue = MutableLiveData(5.0f)
    val shoppingValue = MutableLiveData(5.0f)

    fun confirmClicked() {
        var interestEntity = mapOf("story" to storyValue.value!!,
                                    "art" to artValue.value!!,
                                    "party" to partyValue.value!!,
                                    "nature" to natureValue.value!!,
                                    "entertainment" to entertainmentValue.value!!,
                                    "sport" to sportValue.value!!,
                                    "shopping" to shoppingValue.value!!)
        //chiamata al service per salvare nel db
        user.value!!.interests = interestEntity
        user.value!!.isInitialized = true
        viewModelScope.launch {
            userRepository.setUser(user.value!!)
            navigateBack()
        }
    }

    fun goBackHome() {
        navigate(R.id.action_fragment_interest_to_fragment_home)
    }
}