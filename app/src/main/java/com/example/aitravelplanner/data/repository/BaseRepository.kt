package com.example.aitravelplanner.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

/** Classe repository di base che contiene il riferimento al database Firestore
 *
 */
open class BaseRepository{
    val db = Firebase.firestore
}