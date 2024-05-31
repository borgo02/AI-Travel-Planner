package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.Exclude
import java.util.Date

data class Travel(
    @DocumentId var idTravel: String?,
    @PropertyName("idUser") var idUser: DocumentReference?,
    @PropertyName("info") var info: String?,
    @PropertyName("name") var name: String?,
    @PropertyName("isShared") var isShared: Boolean?,
    @PropertyName("timestamp") var timestamp: Date?,
    @PropertyName("numberOfLikes") var numberOfLikes: Int?,
    @PropertyName("imageUrl") var imageUrl: String?,
    @Exclude @set:Exclude @get:Exclude var stageList: ArrayList<Stage>?,
    @Exclude @set:Exclude @get:Exclude var isLiked: Boolean?
)
