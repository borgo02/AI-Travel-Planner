package com.example.aitravelplanner.data.repository.user

import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    @MockK private lateinit var mockFirestore: FirebaseFirestore
    @MockK private lateinit var mockCollectionReference: CollectionReference
    @MockK private lateinit var mockDocumentReference: DocumentReference
    @MockK private lateinit var mockFirebaseApp: FirebaseApp

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(FirebaseApp::class)
        every { FirebaseApp.getInstance() } returns mockFirebaseApp
        every { FirebaseFirestore.getInstance(mockFirebaseApp) } returns mockFirestore

        every { mockFirestore.collection("users") } returns mockCollectionReference
        every { mockFirestore.collection("travels") } returns mockCollectionReference
        every { mockCollectionReference.document(any()) } returns mockDocumentReference

        userRepository = UserRepository.getInstance().apply {
            this.usersCollectionRef = mockCollectionReference
        }
    }

    @Test
    fun testGetUserReference() = runBlocking {
        val userId = "xotoF1gCuOdGMxgRUX7moQrsbjC2"
        val result = userRepository.getUserReference(userId)

        assertEquals(mockDocumentReference, result)
    }
}
