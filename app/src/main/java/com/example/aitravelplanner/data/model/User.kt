package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.PropertyName

data class User(
    @PropertyName("idUser") var idUser: String,
    @PropertyName("email") var email: String,
    @PropertyName("name") var name: String,
    @PropertyName("surname") var surname: String,
    @PropertyName("createdTravelList") var createdTravelList: ArrayList<Travel>,
    @PropertyName("sharedTravelList") var sharedTravelList: ArrayList<Travel>,
    @PropertyName("likedTravelList") var likedTravelList: ArrayList<Travel>
)
