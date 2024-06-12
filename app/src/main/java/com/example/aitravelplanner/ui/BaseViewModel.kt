package com.example.aitravelplanner.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.example.aitravelplanner.data.model.NavigationCommand
import com.example.aitravelplanner.data.model.User
import com.example.aitravelplanner.data.repository.travel.ITravelRepository
import com.example.aitravelplanner.data.repository.travel.TravelRepository
import com.example.aitravelplanner.data.repository.user.IUserRepository
import com.example.aitravelplanner.data.repository.user.UserRepository
import com.example.aitravelplanner.ui.dashboard.DashboardFragmentDirections
import com.example.aitravelplanner.utils.Event
import com.example.aitravelplanner.utils.getViewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class BaseViewModel @Inject constructor(open val userRepository: IUserRepository = UserRepository.getInstance(), open val travelRepository: ITravelRepository = TravelRepository(), private val coroutineScopeProvider: CoroutineScope? = null) : ViewModel() {
    val currentUser: LiveData<User>
        get() {
            val userLive = MutableLiveData<User>()
            userLive.value = userRepository.getUser() ?: User("", "", "", false)
            return userLive
        }

    private val _isLoading = MutableLiveData<Boolean>()
    private val isTest = true
    val isLoading: LiveData<Boolean> get() = _isLoading
    var isNavigating = false

    private val coroutineScope = getViewModelScope(null)

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun <T> executeWithLoading(
        block: () -> T
    ) {
        if (!isTest)
            _isLoading.value = true

        try {
            block()
        } catch (_: Exception) {
        }
        finally {
            if (!isTest)
                _isLoading.value =false
        }
    }

    protected fun <T> executeWithLoadingSuspend(
        block: suspend () -> T
    ) {
        if (!isTest)
            _isLoading.value = true

        coroutineScope.launch {
            try {
                block()
                withContext(Dispatchers.Main) {
                    if (!isTest)
                        _isLoading.value = false
                }
            } catch (_: Exception) {

            }
            finally {
                withContext(Dispatchers.Main) {
                    if (!isTest)
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

    private fun navigate(navDirections: NavDirections) {
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
        catch (_: Exception)
        {
        }
    }
}