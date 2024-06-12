package com.example.aitravelplanner.data.repository.user

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aitravelplanner.InstantExecutorExtension
import com.example.aitravelplanner.MainCoroutineRule
import com.example.aitravelplanner.ui.dashboard.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@ExtendWith(InstantExecutorExtension::class)
class UserRepositoryTest {

    @get:Rule
    private val testScope = TestScope()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private val userId: String = "idUserTest1"
    private var mockUserRepository: UserRepositoryMock= UserRepositoryMock.getInstance()
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)
    }

    @ExperimentalCoroutinesApi
    @org.junit.jupiter.api.Test
    fun getTravelsByUserTest() = runBlocking {
        val userRepositoryMock = UserRepositoryMock.getInstance()
        val userId = "idUserTest1"

        val result = userRepositoryMock.getTravelsByUser(userId)

        assert(result.size == 1)
    }

    @ExperimentalCoroutinesApi
    @org.junit.jupiter.api.Test
    fun getSharedTravelsByUserTest() = runBlocking {
        val userRepositoryMock = UserRepositoryMock.getInstance()
        val userId = "idUserTest2"

        val result = userRepositoryMock.getSharedTravelsByUser(userId)

        assert(result.size == 2)
    }
}
