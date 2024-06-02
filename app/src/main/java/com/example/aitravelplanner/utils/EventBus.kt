package com.example.aitravelplanner.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

object EventBus {
    private val _profileDataChanged = MutableLiveData<Boolean>()
    val profileDataChanged: LiveData<Boolean> get() = _profileDataChanged

    private val _dashboardDataChanged = MutableLiveData<Boolean>()
    val dashboardDataChanged: LiveData<Boolean> get() = _dashboardDataChanged

    fun notifyProfileDataChanged() {
        _profileDataChanged.value = true
    }

    fun notifyDashboardDataChanged() {
        _dashboardDataChanged.value = true
    }

    fun resetProfileDataChanged() {
        _profileDataChanged.value = false
    }

    fun resetDashboardDataChanged() {
        _dashboardDataChanged.value = false
    }
}