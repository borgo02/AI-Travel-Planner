package com.example.aitravelplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aitravelplanner.data.model.NavigationCommand
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.utils.Event

public abstract class BaseViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
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

    fun navigate(navDirections: Int) {
        _navigation.value = Event(NavigationCommand.ToDirection(navDirections))
    }

    fun navigateBack() {
        _navigation.value = Event(NavigationCommand.Back)
    }
    fun goToInterestFragment() {
        try
        {
            if (!isNavigating)
            {
                isNavigating = true
                navigate(R.id.action_fragment_home_to_fragment_interest)
            }
        }
        catch (e: Exception)
        {
            val ex = e
        }
    }
}