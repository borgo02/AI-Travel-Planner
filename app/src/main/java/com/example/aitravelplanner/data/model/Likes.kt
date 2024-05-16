package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.PropertyName

data class Likes(
    @DocumentId var idLike: String?,
    @PropertyName("idTravel") var idTravel: DocumentReference,
    @PropertyName("timestamp") var timestamp: com.google.firebase.Timestamp
)
