package com.mundocode.pomodoro.ui.screens.habits

import com.mundocode.pomodoro.model.local.Habits

sealed interface HabitsUIState {

    object Loading : HabitsUIState
    data class Error(val throwable: Throwable) : HabitsUIState
    data class Success(val tasks: List<Habits>) : HabitsUIState
}
