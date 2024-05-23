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

public abstract class BaseViewModel : ViewModel() {
    protected val _user = MutableLiveData<User>()
    var user: LiveData<User> = _user
    var isNavigating = false


    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    val userRepository = UserRepository()

    public fun setUser(newUser: User) {
        _user.value = newUser
        if (!newUser.isInitialized)
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
    fun goToInterestFragment() {
        try
        {
            if (false)
            //if (!isNavigating)
            {
                isNavigating = true
                navigate(DashboardFragmentDirections.actionNavigationDashboardToInterest())
            }
        }
        catch (e: Exception)
        {
            val ex = e
        }
    }
}