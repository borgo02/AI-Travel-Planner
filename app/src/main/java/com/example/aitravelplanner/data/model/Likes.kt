package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.PropertyName
import java.sql.Timestamp

data class Likes(
    @PropertyName("idLike") var idLike: String,
    @PropertyName("idTravel") var idTravel: String,
    @PropertyName("idUser") var idUser: String,
    @PropertyName("timestamp") var timestamp: Timestamp
)
