package com.example.aitravelplanner.data.repository.travel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aitravelplanner.utils.InstantExecutorExtension
import com.example.aitravelplanner.utils.MainCoroutineRule
import com.example.aitravelplanner.utils.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@ExtendWith(InstantExecutorExtension::class)
class TravelRepositoryTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)
    }

    @ExperimentalCoroutinesApi
    @org.junit.jupiter.api.Test
    fun getTravelsTest() = runBlocking {
        val travelRepositoryMock = TravelRepositoryMock()
        val result = travelRepositoryMock.getTravels()

        assert(result.size == 3)
    }

    @ExperimentalCoroutinesApi
    @org.junit.jupiter.api.Test
    fun getTravelByIdTest () = runBlocking {
        val travelRepositoryMock = TravelRepositoryMock()
        val travelId = "3"
        val userId = "idUserTest1"
        val result = travelRepositoryMock.getTravelById(travelId, userId)
        
        assert(result != null)
    }
}
