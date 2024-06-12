package com.example.aitravelplanner.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aitravelplanner.InstantTaskExecutorRuleForJUnit5
import com.example.aitravelplanner.MainCoroutineRule
import com.example.aitravelplanner.data.repository.travel.TravelRepositoryMock
import com.example.aitravelplanner.data.repository.user.UserRepositoryMock
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import io.mockk.MockKAnnotations
import io.mockk.junit5.MockKExtension
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.Rule
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.rules.TestRule


@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class DashboardViewModelTest  {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel
    private val userId: String = "1"
    private lateinit var mockUserRepository: UserRepositoryMock
    private lateinit var mockTravelRepository: TravelRepositoryMock


    @BeforeEach
    fun setUp() = runTest(UnconfinedTestDispatcher()) {
        MockKAnnotations.init(this, relaxed = true)
        mockUserRepository = UserRepositoryMock.getInstance()
        mockTravelRepository = TravelRepositoryMock()

        launch{viewModel = DashboardViewModel(mockUserRepository, mockTravelRepository, TravelCardsSingleton(mockTravelRepository, mockUserRepository))}
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSearch() {
        viewModel.searchText.value = "test1"
        viewModel.search()
        assert(viewModel.searchedCardsList.value!!.size == 1)
    }
}