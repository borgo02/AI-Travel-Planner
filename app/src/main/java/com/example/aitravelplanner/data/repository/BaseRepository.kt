package com.example.aitravelplanner.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

open class BaseRepository{
    val db = Firebase.firestore
}