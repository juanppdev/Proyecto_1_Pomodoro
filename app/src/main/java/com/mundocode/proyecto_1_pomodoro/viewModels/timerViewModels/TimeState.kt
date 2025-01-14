package com.mundocode.proyecto_1_pomodoro.ui.state

data class TimeState(
    val remainingTime: Long = 25 * 60 * 1000L,
    val isRunning: Boolean = false
)
