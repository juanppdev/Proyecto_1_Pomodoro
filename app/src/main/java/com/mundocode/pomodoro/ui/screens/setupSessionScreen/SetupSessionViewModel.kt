package com.mundocode.pomodoro.ui.screens.setupSessionScreen

import androidx.lifecycle.ViewModel
import com.mundocode.pomodoro.model.local.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SetupSessionViewModel @Inject constructor() : ViewModel() {

    val sessionState: StateFlow<SessionState>
        field: MutableStateFlow<SessionState> = MutableStateFlow(SessionState())

    fun updateSessionName(sessionName: String) {
        sessionState.update {
            it.copy(
                timer = it.timer.copy(
                    sessionName = sessionName,
                ),
            )
        }
    }

    fun updateMode(mode: String) {
        sessionState.update {
            it.copy(
                timer = it.timer.copy(
                    mode = mode,
                ),
            )
        }
    }

    fun updateTimer(timer: String) {
        sessionState.update {
            it.copy(
                timer = it.timer.copy(
                    timer = timer,
                ),
            )
        }
    }

    fun updatePause(pause: String) {
        sessionState.update {
            it.copy(
                timer = it.timer.copy(
                    pause = pause,
                ),
            )
        }
    }
}

data class SessionState(val timer: Timer) {
    constructor() : this(
        timer = Timer(
            sessionName = "",
            mode = "",
            timer = "30:00",
            pause = "05:00",
        ),
    )
}
