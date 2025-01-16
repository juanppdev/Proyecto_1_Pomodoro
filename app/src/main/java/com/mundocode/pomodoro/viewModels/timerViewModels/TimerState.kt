package com.mundocode.pomodoro.viewModels.timerViewModels

data class TimerState(
    val remainingTime: Long = 25 * 60 * 1000L,
    val isRunning: Boolean = false,
    val isWorking: Boolean = true,
    val workDuration: Long = 25 * 60 * 1000L,
    val breakDuration: Long = 5 * 60 * 1000L,
)
