package com.example.aitravelplanner.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.aitravelplanner.InstantExecutorExtension
import com.example.aitravelplanner.MainCoroutineRule
import com.example.aitravelplanner.data.repository.travel.TravelRepositoryMock
import com.example.aitravelplanner.data.repository.user.UserRepositoryMock
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
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


@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
@ExtendWith(InstantExecutorExtension::class)
class DashboardViewModelTest  {
    @get:Rule
    private val testScope = TestScope()

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
    private val testDispatcher = StandardTestDispatcher()


    @BeforeEach
    fun setUp() = runTest(StandardTestDispatcher()) {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this, relaxed = true)
        mockUserRepository = UserRepositoryMock.getInstance()
        mockTravelRepository = TravelRepositoryMock()
        viewModel = DashboardViewModel(mockUserRepository, mockTravelRepository, TravelCardsSingleton(mockTravelRepository, mockUserRepository), testScope)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testSearch() = runTest{
        viewModel.searchText.value = "test1"
        viewModel.search()
        assert(viewModel.searchedCardsList.value!!.size == 1)
    }
}