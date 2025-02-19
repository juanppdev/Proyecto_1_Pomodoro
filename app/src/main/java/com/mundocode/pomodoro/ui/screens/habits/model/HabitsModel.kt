package com.mundocode.pomodoro.ui.screens.habits.model

data class HabitsModel(
    val id: Int = System.currentTimeMillis().hashCode(),
    val title: String = "",
    val description: String = "",
) {
    // Firestore necesita un constructor vacío
    constructor() : this(0, "", "")
}
