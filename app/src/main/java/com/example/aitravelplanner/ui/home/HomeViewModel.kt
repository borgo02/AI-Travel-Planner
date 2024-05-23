package com.example.aitravelplanner.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aitravelplanner.BaseViewModel
import com.example.aitravelplanner.data.repository.user.UserRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}