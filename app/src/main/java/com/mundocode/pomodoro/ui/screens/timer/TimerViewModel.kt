package com.mundocode.pomodoro.ui.screens.timer

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import com.mundocode.pomodoro.data.sessionDb.SessionDao
import com.mundocode.pomodoro.data.sessionDb.SessionEntity
import com.mundocode.pomodoro.model.local.Timer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(
    private val sessionDao: SessionDao,
    private val pointsRepository: PointsRepository,
) : ViewModel() {

    private val _isPomodoroActive = MutableStateFlow(false)
    val isPomodoroActive: StateFlow<Boolean> = _isPomodoroActive.asStateFlow()

    val userId = FirebaseAuth.getInstance().currentUser?.uid

    val timerState: StateFlow<TimerState>
        field: MutableStateFlow<TimerState> = MutableStateFlow(TimerState())

    private var timerJob: Job? = null

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _sessionHistory = MutableStateFlow<List<SessionHistory>>(emptyList())
    val sessionHistory: StateFlow<List<SessionHistory>> = _sessionHistory.asStateFlow()

    private val _showAnimation = MutableStateFlow(false)
    val showAnimation: StateFlow<Boolean> = _showAnimation.asStateFlow()

    private val _isPomodoroComplete = MutableStateFlow(false)
    val isPomodoroComplete: StateFlow<Boolean> = _isPomodoroComplete.asStateFlow()

    init {
        viewModelScope.launch {
            loadPomodoroStats()
        }
    }

    fun hideAnimation() {
        _showAnimation.value = false
        _isPomodoroComplete.value = false
    }

    fun startTimer(context: Context) {
        if (timerState.value.isRunning) return

        _isPomodoroActive.value = true
        cancelReminderNotification(context) // ✅ Si inicia un Pomodoro, cancelamos la notificación

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            timerState.update { it.copy(isRunning = true) }
            while (timerState.value.remainingTime > 0) {
                delay(1000L)
                timerState.update {
                    it.copy(remainingTime = it.remainingTime - 1000L)
                }
            }
            onTimerFinished(context)
        }
    }

    fun stopTimer(context: Context) {
        timerJob?.cancel()
        timerState.update { it.copy(isRunning = false) }
        scheduleReminderNotification(context) // ✅ Si detiene el Pomodoro, programamos un recordatorio
    }

    fun resetTimer(context: Context) {
        stopTimer(context)
        timerState.update { it.copy(remainingTime = it.workDuration) }
    }

    fun onTimerFinished(context: Context) {
        stopTimer(context)
        _isPomodoroActive.value = false

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            if (!timerState.value.isWorking) { // ✅ Solo marcar Pomodoro completo cuando termina el descanso
                _isPomodoroComplete.value = true
                _showAnimation.value = true
            }

            Timber.tag("Points").e("⏳ Intentando agregar puntos para el usuario: $userId")
            pointsRepository.addPoints(userId.toString(), 100)
            Timber.tag("Points").e("✅ Puntos agregados correctamente")

            if (timerState.value.isWorking) {
                val session = SessionEntity(
                    type = "Trabajo",
                    duration = timerState.value.workDuration.toMinutesString(),
                    date = currentDate,
                )
                sessionDao.insertSession(session)
                showNotification(context, "Sesión Finalizada", "Tiempo de trabajo completado.")

                timerState.update {
                    it.copy(isWorking = false, remainingTime = it.breakDuration, isRunning = false)
                }
                delay(1000) // ✅ Esperar a que la UI refleje los cambios antes de reiniciar
                startTimer(context) // ✅ Iniciar descanso
            } else {
                val session = SessionEntity(
                    type = "Descanso",
                    duration = timerState.value.breakDuration.toMinutesString(),
                    date = currentDate,
                )
                sessionDao.insertSession(session)
                showNotification(context, "Descanso Terminado", "Tiempo de descanso finalizado.")
                _navigateToHome.value = true
            }
        }

        viewModelScope.launch {
            delay(3000) // ✅ Mostrar la animación por 3 segundos antes de ocultarla
            _showAnimation.value = false
        }
    }

    fun setupTimer(timer: Timer) {
        timerState.update {
            it.copy(
                sessionName = timer.sessionName,
                mode = timer.mode,
                workDuration = timer.timer.toMillis(),
                breakDuration = timer.pause.toMillis(),
                remainingTime = timer.timer.toMillis(),
                isRunning = false,
                isWorking = true,
            )
        }
    }

    fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("session_channel", "Sesiones", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "session_channel")
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.timer)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    fun onPopupDismissed() {
        _navigateToHome.value = false
    }

    fun scheduleReminderNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val triggerTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 30) // ✅ Notificación en 30 minutos si no se inicia Pomodoro
        }.timeInMillis

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    fun cancelReminderNotification(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        alarmManager.cancel(pendingIntent)
    }

    private val _pomodoroCount = MutableStateFlow(0)
    val pomodoroCount: StateFlow<Int> = _pomodoroCount.asStateFlow()

    private fun loadPomodoroStats() {
        val startDate = getStartDate()
        val endDate = getEndDate()
        viewModelScope.launch {
            sessionDao.getPomodoroCountBetweenDates(startDate, endDate).collectLatest { count ->
                _pomodoroCount.value = count
            }
        }
    }

    private fun getStartDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)
    }

    private fun getEndDate(): String {
        val calendar = Calendar.getInstance()
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(calendar.time)
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
