package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.PropertyName
import java.io.Serializable

data class User(
    @PropertyName("idUser") var idUser: String,
    @PropertyName("email") var email: String,
    @PropertyName("firstname") var firstname: String,
    @PropertyName("lastname") var lastname: String,
    @PropertyName("isInitialized") var isInitialized: Boolean,
    @PropertyName("interests") var interests: Map<String, Int>
) : Serializable
