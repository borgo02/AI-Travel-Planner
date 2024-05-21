package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class User(
    @DocumentId var idUser: String = "",
    @PropertyName("email") var email: String = "",
    @PropertyName("fullname") var fullname: String = "",
    @PropertyName("isInitialized") var isInitialized: Boolean = false,
    @PropertyName("interests") var interests: Map<String, Float>? = null,
    @PropertyName("likedTravels") var likedTravels: ArrayList<Likes>? = null
    ) : Serializable
