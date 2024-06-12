package com.example.aitravelplanner.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope

/**
 * Configure CoroutineScope injection for production and testing.
 *
 * @receiver ViewModel provides viewModelScope for production
 * @param coroutineScope null for production, injects TestCoroutineScope for unit tests
 * @return CoroutineScope to launch coroutines on
 */
fun ViewModel.getViewModelScope(coroutineScope: CoroutineScope?) =
    coroutineScope ?: this.viewModelScope