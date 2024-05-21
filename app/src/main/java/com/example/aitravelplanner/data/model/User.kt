package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

data class User(
    @DocumentId var idUser: String?,
    @PropertyName("email") var email: String?,
    @PropertyName("fullname") var fullname: String?,
    @PropertyName("interests") var interests: Map<*, *>,
    @PropertyName("likedTravels") var likedTravels: ArrayList<Likes>?
)
