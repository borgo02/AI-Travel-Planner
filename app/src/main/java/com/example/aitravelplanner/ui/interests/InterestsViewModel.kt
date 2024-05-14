package com.example.aitravelplanner.ui.interests

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.BaseViewModel

class InterestsViewModel : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var fragmentManager: FragmentManager? = null

    val storyValue = MutableLiveData(5.0f)
    val artValue = MutableLiveData(5.0f)
    val partyValue = MutableLiveData(5.0f)
    val natureValue = MutableLiveData(5.0f)
    val entertainmentValue = MutableLiveData(5.0f)
    val sportValue = MutableLiveData(5.0f)
    val shoppingValue = MutableLiveData(5.0f)

    fun confirmClicked() {
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.remove(InterestsFragment())
        fragmentTransaction.commit()
        var interestEntity = mapOf("story" to storyValue.value,
                                    "art" to artValue.value,
                                    "party" to partyValue.value,
                                    "nature" to natureValue.value,
                                    "entertainment" to entertainmentValue.value,
                                    "sport" to sportValue.value,
                                    "shopping" to shoppingValue.value)
        //chiamata al service per salvare nel db
    }
}