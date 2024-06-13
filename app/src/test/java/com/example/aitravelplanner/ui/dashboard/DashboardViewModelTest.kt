package com.example.aitravelplanner.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aitravelplanner.utils.InstantExecutorExtension
import com.example.aitravelplanner.utils.MainCoroutineRule
import com.example.aitravelplanner.data.repository.travel.TravelRepositoryMock
import com.example.aitravelplanner.data.repository.user.UserRepositoryMock
import com.example.aitravelplanner.ui.components.travelCard.CardTravel
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import com.example.aitravelplanner.utils.MainDispatcherRule
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule
import kotlinx.coroutines.test.advanceUntilIdle

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@ExtendWith(InstantExecutorExtension::class)
class DashboardViewModelTest  {
    private val testScope = TestScope()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private var mockUserRepository: UserRepositoryMock= UserRepositoryMock.getInstance()
    private var mockTravelRepository: TravelRepositoryMock = TravelRepositoryMock()
    private lateinit var viewModel: DashboardViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)
        viewModel = DashboardViewModel(mockUserRepository, mockTravelRepository, TravelCardsSingleton(mockTravelRepository, mockUserRepository), testScope)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSearch() = runTest{
        viewModel.initialize().await()
        advanceUntilIdle() // Runs the new coroutine
        require(viewModel.initialized.get())
        viewModel.searchText.value = "test1"
        viewModel.search()
        assert(viewModel.searchedCardsList.value!!.size == 1)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testClickLike() = runTest {
        viewModel.initialize().await()
        advanceUntilIdle()
        require(viewModel.initialized.get())
        val selectedTravel = CardTravel(username = "User", userImage = "prova immagine", travelImage = "prova immagine", travelName = "Roma", affinityPerc = "100", travelLikes = 10, timestamp = "", isLiked = false, info = "prova info", stageCardList = arrayListOf(), userId = "1", travelId = "1", isShared = true)
        viewModel._selectedTravel.value = selectedTravel
        viewModel.clickLike()
        assert(viewModel.selectedTravel.value!!.isLiked)
    }
}