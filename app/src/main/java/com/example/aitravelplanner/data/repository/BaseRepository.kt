package com.example.aitravelplanner.data.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import io.mockk.impl.annotations.MockK
import java.security.KeyStore.TrustedCertificateEntry

@ExtendWith(MockKExtension::class)
open class BaseRepository(var isTest: Boolean = true) {
    lateinit var db: FirebaseFirestore
    @MockK  lateinit var mockDb: FirebaseFirestore
    init {
        if(!isTest){
            db = Firebase.firestore
        }
        else
            db = mockDb

    }
}