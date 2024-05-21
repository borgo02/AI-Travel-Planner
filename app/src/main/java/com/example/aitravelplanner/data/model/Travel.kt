package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.Timestamp
import java.util.Date

data class Travel(
    @DocumentId var idTravel: String?,
    @PropertyName("idUser") var idUser: String?,
    @PropertyName("info") var info: String?,
    @PropertyName("name") var name: String?,
    @PropertyName("isShared") var isShared: Boolean?,
    @PropertyName("timestamp") var timestamp: Date?,
    @PropertyName("numberOfLikes") var numberOfLikes: Int?,
    @PropertyName("imageUrl") var imageUrl: String?,
    @PropertyName("stages") var stageList: ArrayList<Stage>?,
    var isLiked: Boolean?
)
