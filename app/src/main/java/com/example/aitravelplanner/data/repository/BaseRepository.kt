package com.example.aitravelplanner.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class BaseRepository{
    val databaseRef = Firebase.firestore
}