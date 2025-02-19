package com.mundocode.pomodoro.core.navigation

import com.kiwi.navigationcompose.typed.Destination
import com.mundocode.pomodoro.model.local.Timer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed interface Destinations : Destination {

    @Serializable
    data object Splash : Destinations

    @Serializable
    data object Login : Destinations

    @Serializable
    data object HomeScreen : Destinations

    @Serializable
    data object SetupSessionScreen : Destinations

    @Serializable
    data class TimerScreen(@Contextual val timer: Timer) : Destinations

    @Serializable
    data object TaskScreen : Destinations

    @Serializable
    data object HabitsScreen : Destinations
}
