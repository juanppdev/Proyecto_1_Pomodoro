package com.mundocode.pomodoro.core.navigation

import kotlinx.serialization.Serializable

sealed interface Destinations {

    @Serializable
    data object Home : Destinations

    @Serializable
    data object SetupSession : Destinations

    @Serializable
    data object Timer : Destinations

    @Serializable
    data object Task : Destinations
  
    @Serializable
    data object Habits : Destinations
}
