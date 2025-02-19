package com.mundocode.pomodoro.ui.screens.timer

data class TimerState(
    val sessionName: String = "",
    val mode: String = "",
    val remainingTime: Long = 25 * 60 * 1000L,
    val workDuration: Long = 25 * 60 * 1000L,
    val breakDuration: Long = 5 * 60 * 1000L,
    val isRunning: Boolean = false,
    val isWorking: Boolean = true,
)
