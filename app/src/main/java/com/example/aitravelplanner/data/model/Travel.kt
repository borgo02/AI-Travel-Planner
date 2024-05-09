package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.PropertyName

data class Travel(
    @PropertyName("idTravel") var idTravel: String,
    @PropertyName("name") var name: String,
    @PropertyName("isShared") var isShared: Boolean,
    @PropertyName("timestamp") var timestamp: String,
    @PropertyName("numberOfLikes") var numberOfLikes: Int,
    @PropertyName("imageUrl") var imageUrl: String,
    @PropertyName("stageList") var stageList: ArrayList<Stage>
)
