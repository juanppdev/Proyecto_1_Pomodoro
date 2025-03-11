package com.mundocode.pomodoro.model.local

data class Habits(
    val id: Int = System.currentTimeMillis().hashCode(),
    val title: String = "",
    val description: String = "",
)
