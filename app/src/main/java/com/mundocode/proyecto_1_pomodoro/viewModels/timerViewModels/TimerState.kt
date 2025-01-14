package com.mundocode.proyecto_1_pomodoro.ui.state

data class TimerState(
    val remainingTime: Long = 25 * 60 * 1000L,
    val isRunning: Boolean = false,
    val isWorking: Boolean = true,
    val workDuration: Long = 25 * 60 * 1000L,
)
