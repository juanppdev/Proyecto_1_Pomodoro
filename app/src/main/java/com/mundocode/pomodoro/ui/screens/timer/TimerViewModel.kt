package com.mundocode.pomodoro.ui.screens.timer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.sessionDb.SessionDao
import com.mundocode.pomodoro.data.sessionDb.SessionEntity
import com.mundocode.pomodoro.model.local.Timer
import com.mundocode.pomodoro.ui.screens.homeScreen.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val sessionDao: SessionDao) : ViewModel() {

    val timerState: StateFlow<TimerState>
        field: MutableStateFlow<TimerState> = MutableStateFlow(TimerState())

    private var timerJob: Job? = null

    // Lista de sesiones completadas
    private val _sessionHistory = MutableStateFlow<List<SessionHistory>>(emptyList())
    val sessionHistory: StateFlow<List<SessionHistory>> = _sessionHistory.asStateFlow()

    fun startTimer() {
        if (timerState.value.isRunning) return

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            timerState.update { it.copy(isRunning = true) }
            while (timerState.value.remainingTime > 0) {
                delay(1000L)
                timerState.update {
                    it.copy(remainingTime = it.remainingTime - 1000L)
                }
            }
            onTimerFinished()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerState.update { it.copy(isRunning = false) }
    }

    fun resetTimer() {
        stopTimer()
        timerState.update { it.copy(remainingTime = it.workDuration) }
    }

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private fun onTimerFinished() {
        stopTimer()
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            if (timerState.value.isWorking) {
                val session = SessionEntity(
                    type = "Trabajo",
                    duration = timerState.value.workDuration.toMinutesString(),
                    date = currentDate,
                )
                sessionDao.insertSession(session) // Guardar en Room

                // ✅ Forzar la recarga de datos en HomeViewModel
                HomeViewModel(sessionDao).loadSessions("Weekly")
                Log.d("TimerViewModel", "Sesión guardada: $session")

                // Cambiar a descanso
                timerState.update {
                    it.copy(
                        isWorking = false,
                        remainingTime = it.breakDuration,
                        isRunning = false,
                    )
                }
                startTimer()
            } else {
                val session = SessionEntity(
                    type = "Descanso",
                    duration = timerState.value.breakDuration.toMinutesString(),
                    date = currentDate,
                )
                sessionDao.insertSession(session) // Guardar en Room

                // ✅ Forzar la recarga de datos en HomeViewModel
                HomeViewModel(sessionDao).loadSessions("Weekly")
                Log.d("TimerViewModel", "Sesión guardada: $session")

                _navigateToHome.value = true // Activar la navegación a Home
            }
        }
    }

    fun onPopupDismissed() {
        _navigateToHome.value = false
    }

    fun setupTimer(timer: Timer) {
        timerState.update {
            it.copy(
                sessionName = timer.sessionName,
                mode = timer.mode,
                workDuration = timer.timer.toMillis(),
                breakDuration = timer.pause.toMillis(),
                remainingTime = timer.timer.toMillis(),
                isRunning = false, // Asegura que comience detenido
                isWorking = true,
            )
        }
    }
}

fun Long.toMinutesString(): String {
    val minutes = this / 1000 / 60
    val seconds = (this / 1000 % 60).toString().padStart(2, '0')
    return "$minutes:$seconds"
}

fun String.toMillis(): Long {
    val parts = this.split(":")
    if (parts.size != 2) return 0L
    val minutes = parts[0].toLongOrNull() ?: return 0L
    val seconds = parts[1].toLongOrNull() ?: return 0L
    return (minutes * 60 + seconds) * 1000L
}

data class SessionHistory(val type: String, val duration: String, val date: String)
