package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.aitravelplanner.data.model.NavigationCommand
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.dashboard.DashboardFragmentDirections
import com.example.aitravelplanner.utils.Event
import javax.inject.Inject
import javax.inject.Singleton

public open class BaseViewModel @Inject constructor() : ViewModel() {
    val userRepository = UserRepository()
    val currentUser: LiveData<User>
        get() {
            val userLive = MutableLiveData<User>()
            userLive.value = userRepository.getUser() ?: User("", "", "", false)
            return userLive
        }
    var isNavigating = false


    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun checkIfUserHaveInterest() {
        if (!currentUser.value!!.isInitialized)
        {
            goToInterestFragment()
        }
    }

    fun navigate(navDirections: NavDirections) {
        _navigation.value = Event(NavigationCommand.ToDirection(navDirections))
    }

    fun navigateBack() {
        _navigation.value = Event(NavigationCommand.Back)
    }
    private fun goToInterestFragment() {
        try
        {
            navigate(DashboardFragmentDirections.actionNavigationDashboardToInterest())
        }
        catch (e: Exception)
        {
            val ex = e
        }
    }
}