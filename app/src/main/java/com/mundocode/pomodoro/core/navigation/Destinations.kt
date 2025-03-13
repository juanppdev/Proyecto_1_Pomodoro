package com.mundocode.pomodoro.core.navigation

import com.kiwi.navigationcompose.typed.Destination
import com.mundocode.pomodoro.model.local.Timers
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed interface Destinations : Destination {

    @Serializable
    data object Splash : Destinations

    @Serializable
    data object Login : Destinations

    @Serializable
    data object Register : Destinations

    @Serializable
    data object Home : Destinations

    @Serializable
    data object SetupSession : Destinations

    @Serializable
    data class Timer(@Contextual val timer: Timers) : Destinations

    @Serializable
    data object Task : Destinations

    @Serializable
    data object Habits : Destinations

    @Serializable
    data object Store : Destinations
}
