package com.mundocode.pomodoro.core.navigation

import com.kiwi.navigationcompose.typed.Destination
import kotlinx.serialization.Serializable

sealed interface Destinations : Destination {

    @Serializable
    data object Login : Destinations

    @Serializable
    data object Home : Destinations

    @Serializable
    data object SetupSession : Destinations

    @Serializable
    data object Timer : Destinations
}
