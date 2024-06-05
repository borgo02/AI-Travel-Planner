package com.example.aitravelplanner.ui.dashboard

import com.example.aitravelplanner.MainCoroutineRule
import com.example.aitravelplanner.data.model.Travel
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.user.UserRepository
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
    @MockK  private lateinit var mockUserRepository: UserRepository
    @MockK  private lateinit var mockTravelRepository: TravelRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { mockUserRepository.getUser() } returns User(
            userId,
            "test@test",
            "test",
            true,
            mapOf("interest1" to 1.5.toFloat(),
            "interest2" to 2.5.toFloat(),
            "interest3" to 3.5.toFloat())
        )

        coEvery  { mockUserRepository.getUserReference(userId) } returns mockk(relaxed = true)

        coEvery  { mockTravelRepository.getSharedTravels(userId) } returns arrayListOf(
            Travel("1",
                idUser = mockk(name = "2"),
                name = "test1",
                imageUrl = "test1",
                numberOfLikes = 1,
                timestamp = Date(),
                isLiked = true,
                info = "test1",
                isShared = true,
                stageList = arrayListOf()
            ))

        coEvery  { mockUserRepository.getNotSharedTravelsByUser(userId) } returns arrayListOf(
            Travel("2",
                idUser = mockk(userId),
                name = "test2",
                imageUrl = "test2",
                numberOfLikes = 1,
                timestamp = Date(),
                isLiked = true,
                info = "test2",
                isShared = true,
                stageList = arrayListOf()
            ))

            /*TravelCardsSingleton.apply {
                travelRepository = mockTravelRepository
                userRepository = mockUserRepository
            }*/

            // Initialize the DashboardViewModel
            viewModel = DashboardViewModel().apply {
                userRepository = mockUserRepository
                travelRepository = mockTravelRepository}
        }

    @Test
    fun testSearch() = runBlocking {
        viewModel.searchText.value = "test1"
        viewModel.search()
        assert(viewModel.searchedCardsList.value!!.size == 1)
    }
}