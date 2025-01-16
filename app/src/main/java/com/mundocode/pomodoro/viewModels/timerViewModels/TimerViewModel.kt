package com.mundocode.pomodoro.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.viewModels.timerViewModels.TimerState
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
            if (_timerState.value.remainingTime <= 0) {
                onTimerFinished()
            }
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

    fun updateBreakDuration(newDuration: Long) {
        _timerState.value = _timerState.value.copy(
            breakDuration = newDuration,
        )
    }

    private fun onTimerFinished() {
        stopTimer()

        if (_timerState.value.isWorking) {
            // Cambiamos el estado a descanso
            _timerState.value = _timerState.value.copy(
                isWorking = false,
                remainingTime = _timerState.value.breakDuration,
                isRunning = false
            )
            // Iniciar automáticamente el descanso si es necesario
            startTimer()
        } else {
            // Volver al trabajo
            _timerState.value = _timerState.value.copy(
                isWorking = true,
                remainingTime = _timerState.value.workDuration,
                isRunning = false
            )
            // Iniciar automáticamente el trabajo
            startTimer()
        }
    }
}

