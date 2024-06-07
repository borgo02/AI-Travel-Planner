package com.example.aitravelplanner.ui.dashboard

import com.example.aitravelplanner.MainCoroutineRule
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.travel.TravelRepositoryMock
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.data.repository.user.UserRepositoryMock
import com.example.aitravelplanner.ui.travel.TravelCardsSingleton
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.Date

@ExperimentalCoroutinesApi
@ExtendWith(MockKExtension::class)
class DashboardViewModelTest {
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()


    private lateinit var viewModel: DashboardViewModel
    private val userId: String = "1"
    private lateinit var mockUserRepository: UserRepositoryMock
    private lateinit var mockTravelRepository: TravelRepositoryMock

    @Before
    fun setUp() {
        mockUserRepository = UserRepositoryMock.getInstance()
        mockTravelRepository = TravelRepositoryMock()

        viewModel = DashboardViewModel(mockUserRepository, mockTravelRepository, TravelCardsSingleton(mockUserRepository, mockTravelRepository))
    }

    @Test
    fun testSearch() = runBlocking {
        viewModel.searchText.value = "test1"
        viewModel.search()
        assert(viewModel.searchedCardsList.value!!.size == 1)
    }
}