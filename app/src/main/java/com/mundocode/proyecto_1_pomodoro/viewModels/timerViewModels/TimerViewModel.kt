package com.mundocode.proyecto_1_pomodoro.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.proyecto_1_pomodoro.ui.state.TimerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {
    private val _timerState = mutableStateOf(TimerState())
    val timerState: State<TimerState> get() = _timerState

    private var timerJob: Job? = null

    fun startTimer() {
        if (_timerState.value.isRunning) return

        timerJob = viewModelScope.launch {
            _timerState.value = _timerState.value.copy(isRunning = true)
            while (_timerState.value.remainingTime > 0) {
                delay(1000L)
                _timerState.value = _timerState.value.copy(
                    remainingTime = _timerState.value.remainingTime - 1000L,
                )
            }
            stopTimer()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _timerState.value = _timerState.value.copy(isRunning = false)
    }

    fun resetTimer() {
        stopTimer()
        _timerState.value = _timerState.value.copy(
            remainingTime = _timerState.value.workDuration,
        )
    }

    fun updateWorkDuration(newDuration: Long) {
        _timerState.value = _timerState.value.copy(
            workDuration = newDuration,
            remainingTime = newDuration,
        )
    }
}
