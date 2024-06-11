package com.example.aitravelplanner.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName

/** Data class che modella la singola tappa collegata ad uno specifico viaggio
 *
 */
data class Stage(
    @DocumentId var idStage: String?,
    @PropertyName("name") var name: String,
    @PropertyName("imageUrl") var imageUrl: String,
    @PropertyName("city") var city: String,
    @PropertyName("description") var description: String,
    @PropertyName("position") var position: Int
)
