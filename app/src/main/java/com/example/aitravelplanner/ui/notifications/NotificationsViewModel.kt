package com.example.aitravelplanner.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.data.repository.user.UserRepository
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(userRepository: UserRepository) : BaseViewModel(userRepository) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}