package com.example.aitravelplanner.data.model

import androidx.navigation.NavDirections

sealed class NavigationCommand {
    data class ToDirection(val directions: Int) : NavigationCommand()
    object Back : NavigationCommand()
}