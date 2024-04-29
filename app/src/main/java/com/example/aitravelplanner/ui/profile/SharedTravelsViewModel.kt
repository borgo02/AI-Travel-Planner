package com.example.aitravelplanner.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.ui.components.CardTravel

class SharedTravelsViewModel: ViewModel() {
    private val _cardTravelList = MutableLiveData<ArrayList<CardTravel>>()
    private val _likedTravel = MutableLiveData<Boolean>()

    private val _username = MutableLiveData<String>("")
    private val _userImage = MutableLiveData<String>("")
    private val _travelImage = MutableLiveData<String>("")
    private val _travelName = MutableLiveData<String>("")
    private val _travelAffinity = MutableLiveData<String>("")
    private val _affinityImage = MutableLiveData<String>("")
    private val _travelLike = MutableLiveData<Int>(0)
    private val _likesImage = MutableLiveData<String>("")
    private val _shareImage = MutableLiveData<String>("")
    private val _timestamp = MutableLiveData<String>("")

    val username: LiveData<String>
        get() = _username
    val userImage: LiveData<String>
        get() = _userImage
    val travelImage: LiveData<String>
        get() = _travelImage
    val travelName: LiveData<String>
        get() = _travelName
    val travelAffinity: LiveData<String>
        get() = _travelAffinity
    val affinityImage: LiveData<String>
        get() = _affinityImage
    val travelLike: LiveData<Int>
        get() = _travelLike
    val likesImage: LiveData<String>
        get() = _likesImage
    val shareImage: LiveData<String>
        get() = _shareImage
    val timestamp: LiveData<String>
        get() = _timestamp
    val cardTravelList: LiveData<ArrayList<CardTravel>>
        get() = _cardTravelList
    val likedTravel: LiveData<Boolean>
        get() = _likedTravel
}