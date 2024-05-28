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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

public open class BaseViewModel @Inject constructor() : ViewModel() {
    val userRepository = UserRepository.getInstance()
    val currentUser: LiveData<User>
        get() {
            val userLive = MutableLiveData<User>()
            userLive.value = userRepository.getUser() ?: User("", "", "", false)
            return userLive
        }

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    var isNavigating = false


    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun <T> executeWithLoading(
        block: () -> T
    ) {
        _isLoading.value = true

        try {
            block()
        } catch (_: Exception) {
        }
        finally {
            _isLoading.value =false
        }
    }

    protected fun <T> executeWithLoadingSuspend(
        block: suspend () -> T
    ) {
        _isLoading.value = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                block()
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            } catch (_: Exception) {

            }
            finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }

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