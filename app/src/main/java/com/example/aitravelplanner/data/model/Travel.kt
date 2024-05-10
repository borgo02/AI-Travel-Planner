package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.PropertyName
import java.sql.Timestamp

data class Travel(
    @PropertyName("idTravel") var idTravel: String,
    @PropertyName("idUser") var idUser: String,
    @PropertyName("info") var info: String,
    @PropertyName("name") var name: String,
    @PropertyName("isShared") var isShared: Boolean,
    @PropertyName("timestamp") var timestamp: Timestamp,
    @PropertyName("numberOfLikes") var numberOfLikes: Int,
    @PropertyName("imageUrl") var imageUrl: String,
    @PropertyName("stageList") var stageList: ArrayList<Stage>
)
