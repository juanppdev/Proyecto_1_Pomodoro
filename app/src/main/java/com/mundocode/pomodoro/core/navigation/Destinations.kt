package com.mundocode.pomodoro.core.navigation

import com.kiwi.navigationcompose.typed.Destination
import com.mundocode.pomodoro.model.local.Timer
import kotlinx.serialization.Serializable

sealed interface Destinations : Destination {

    @Serializable
    data object Login : Destinations

    @Serializable
    data object HomeScreen : Destinations

    @Serializable
    data object SetupSessionScreen : Destinations

    @Serializable
    data class TimerScreen(val timer: Timer) : Destinations

    @Serializable
    data object Task : Destinations

    @Serializable
    data object Habits : Destinations
}
