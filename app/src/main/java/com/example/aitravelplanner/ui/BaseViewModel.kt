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
    val isLoading: LiveData<Boolean> get() = _isLoading
    var isNavigating = false

    val coroutineScope = getViewModelScope(null)
    private var _isBusy: ArrayList<Int> = ArrayList<Int>()
    private var _isBusyLock: Any = Any()

    private fun addBusy()
    {
        synchronized(_isBusyLock) {
            _isBusy.add(1)
        }
    }

    private fun popBusy(): Boolean
    {
        synchronized(_isBusyLock) {
            _isBusy.removeLastOrNull()
            return _isBusy.isEmpty()
        }
    }

    private val _navigation = MutableLiveData<Event<NavigationCommand>>()
    val navigation: LiveData<Event<NavigationCommand>> get() = _navigation

    fun <T> executeWithLoading(
        block: () -> T
    ) {
        _isLoading.value = true
        addBusy()
        try {
            block()
        } catch (_: Exception) {
        }
        finally {
            if (popBusy())
                _isLoading.value = false
        }
    }

    protected fun <T> executeWithLoadingSuspend(
        block: suspend () -> T
    ) {
        _isLoading.value = true
        addBusy()
        coroutineScope.launch {
            try {
                block()
                withContext(Dispatchers.Main) {
                    val status = popBusy()
                    if (status)
                        _isLoading.value = false
                }
            } catch (_: Exception) {

            }
            finally {
                withContext(Dispatchers.Main) {
                    if (popBusy())
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
        catch (_: Exception)
        {
        }
    }
}