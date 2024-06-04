package com.example.aitravelplanner.data.repository.user

import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Date

@ExtendWith(MockKExtension::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    @MockK private lateinit var mockFirestore: FirebaseFirestore
    @MockK private lateinit var mockUserCollectionReference: CollectionReference
    @MockK private lateinit var mockTravelCollectionReference: CollectionReference
    @MockK private lateinit var mockLikedTravelsCollectionReference: CollectionReference
    @MockK private lateinit var mockUserDocumentReference: DocumentReference
    @MockK private lateinit var mockFirebaseApp: FirebaseApp
    @MockK private lateinit var mockTravelRepository: TravelRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        mockkStatic(FirebaseApp::class)
        every { FirebaseApp.getInstance() } returns mockFirebaseApp
        every { FirebaseFirestore.getInstance(mockFirebaseApp) } returns mockFirestore

        every { mockFirestore.collection("users") } returns mockUserCollectionReference
        every { mockFirestore.collection("travels") } returns mockTravelCollectionReference
        every { mockUserCollectionReference.document(any()) } returns mockUserDocumentReference
        every { mockUserDocumentReference.collection("likedTravels") } returns mockLikedTravelsCollectionReference

        userRepository = UserRepository.getInstance().apply {
            this.usersCollectionRef = mockUserCollectionReference
            this.travelsCollectionReference = mockTravelCollectionReference
            this.travelRepository = mockTravelRepository
        }
    }

    @Test
    fun testGetUserReference() = runBlocking {
        val userId = "xotoF1gCuOdGMxgRUX7moQrsbjC2"
        val result = userRepository.getUserReference(userId)

        assertEquals(mockUserDocumentReference, result)
    }

    @Test
    fun getTravelsByUser() = runBlocking {
        val userId = "xotoF1gCuOdGMxgRUX7moQrsbjC2"
        val travelId = "travelId"

        val mockUserRef = mockk<DocumentReference>()
        val mockTravelRef = mockk<QuerySnapshot>()
        val mockTask = mockk<Task<QuerySnapshot>>()

        every { mockUserCollectionReference.document(userId) } returns mockUserRef
        every { mockTravelCollectionReference.whereEqualTo("idUser", mockUserRef).get() } returns mockTask
        every { mockTask.isSuccessful } returns true
        every { mockTask.isComplete } returns true
        every { mockTask.result } returns mockTravelRef
        every { mockTask.exception } returns null
        every { mockTask.isCanceled } returns false
        every { mockTravelRef.documents } returns listOf(mockk {
            every { id } returns travelId
        })

        val mockTravel = Travel(
            idTravel = travelId,
            idUser = mockUserRef,
            info = "info",
            name = "name",
            isShared = true,
            timestamp = Date(),
            numberOfLikes = 0,
            imageUrl = "url",
            stageList = arrayListOf(),
            isLiked = false
        )

        val travelRepositoryMock = mockk<TravelRepository> {
            coEvery { getTravelById(travelId, userId) } returns mockTravel
        }

        userRepository = UserRepository.getInstance().apply {
            this.usersCollectionRef = mockUserCollectionReference
            this.travelRepository = travelRepositoryMock
        }

        val result = userRepository.getTravelsByUser(userId)

        assertEquals(1, result.size)
        assertEquals(mockTravel, result[0])
    }
}
