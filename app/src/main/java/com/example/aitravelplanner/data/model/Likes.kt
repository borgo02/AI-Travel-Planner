package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName
import java.io.Serializable
import java.util.Date

data class Likes(
    @DocumentId var idLike: String?,
    @PropertyName("idTravel") var idTravel: DocumentReference,
    @PropertyName("timestamp") var timestamp: Date?
):Serializable
