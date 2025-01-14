package com.mundocode.proyecto_1_pomodoro.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.proyecto_1_pomodoro.ui.state.TimeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimeViewModel : ViewModel() {
    private val _timeState = androidx.compose.runtime.mutableStateOf(TimeState())
    val timeState: androidx.compose.runtime.State<TimeState> get() = _timeState

    private var timerJob: kotlinx.coroutines.Job? = null

    fun startTimer() {
        if (_timeState.value.isRunning) return

        timerJob = viewModelScope.launch {
            _timeState.value = _timeState.value.copy(isRunning = true)
            while (_timeState.value.remainingTime > 0) {
                delay(1000L) // Espera 1 segundo
                _timeState.value = _timeState.value.copy(
                    remainingTime = _timeState.value.remainingTime - 1000L
                )
            }
            stopTimer()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timeState.value = _timeState.value.copy(isRunning = false)
    }

    fun resetTimer() {
        stopTimer()
        _timeState.value = _timeState.value.copy(
            remainingTime = _timeState.value.workDuration
        )
    }

    fun updateWorkDuration(durationMinutes: Int) {
        val newDuration = durationMinutes * 60 * 1000L
        _timeState.value = _timeState.value.copy(
            workDuration = newDuration,
            remainingTime = newDuration
        )
    }
}
