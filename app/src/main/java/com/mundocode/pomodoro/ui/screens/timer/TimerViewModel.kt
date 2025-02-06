package com.mundocode.pomodoro.ui.screens.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.model.local.Timer
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimerViewModel : ViewModel() {

    val timerState: StateFlow<TimerState>
        field: MutableStateFlow<TimerState> = MutableStateFlow(TimerState())

    private var timerJob: Job? = null

    fun startTimer() {
        if (timerState.value.isRunning) return

        timerJob = viewModelScope.launch {
            timerState.update { it.copy(isRunning = true) }
            while (timerState.value.remainingTime > 0) {
                delay(1000L)
                timerState.update {
                    it.copy(remainingTime = it.remainingTime - 1000L)
                }
            }
            if (timerState.value.remainingTime <= 0) {
                onTimerFinished()
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        stopTimer()
        timerState.update { it.copy(remainingTime = it.workDuration) }
//        timerState.value = timerState.value.copy(
//            remainingTime = timerState.value.workDuration,
//        )
    }

    fun updateWorkDuration(newDuration: Long) {
        timerState.update {
            it.copy(
                workDuration = newDuration,
                remainingTime = newDuration,
            )
        }
    }

    fun updateBreakDuration(newDuration: Long) {
        timerState.value = timerState.value.copy(
            breakDuration = newDuration,
        )
    }

    private fun onTimerFinished() {
        stopTimer()

        if (timerState.value.isWorking) {
            // Cambiamos el estado a descanso
            timerState.update {
                it.copy(
                    isWorking = false,
                    remainingTime = it.breakDuration,
                    isRunning = false,
                )
            }
            // Iniciar automáticamente el descanso si es necesario
            startTimer()
        } else {
            // Volver al trabajo
            timerState.update {
                it.copy(
                    isWorking = true,
                    remainingTime = it.workDuration,
                    isRunning = false,
                )
            }
            // Iniciar automáticamente el trabajo
            startTimer()
        }
    }

    fun setupTimer(timer: Timer) {
        Log.d("TimerViewModel", "setupTimer: $timer")
        timerState.update {
            it.copy(
                remainingTime = it.workDuration,
                workDuration = it.workDuration,
                breakDuration = timer.pause.toMillis(),
            )
        }
    }
}

/**
 * Check the string is timer format (mm:ss) and convert it to milliseconds
 */
fun String.toMillis(): Long {
    val parts = this.split(":")
    if (parts.size != 2) return 0L
    val minutes = parts[0].toLongOrNull() ?: return 0L
    val seconds = parts[1].toLongOrNull() ?: return 0L
    return (minutes * 60 + seconds) * 1000L
}
