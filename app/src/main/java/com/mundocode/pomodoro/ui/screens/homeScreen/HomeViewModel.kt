package com.mundocode.pomodoro.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.sessionDb.SessionDao
import com.mundocode.pomodoro.data.sessionDb.SessionEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val sessionDao: SessionDao) : ViewModel() {

    private val _filter = MutableStateFlow("Weekly") // ✅ Mantener el estado actual
    val filter: StateFlow<String> = _filter.asStateFlow()

    private val _sessionsData = MutableStateFlow<Map<String, Float>>(emptyMap())
    val sessionsData: StateFlow<Map<String, Float>> = _sessionsData.asStateFlow()

    private val _xLabels = MutableStateFlow<List<String>>(emptyList())
    val xLabels: StateFlow<List<String>> = _xLabels.asStateFlow()

    private val _totalTimeData = MutableStateFlow<Map<String, Float>>(emptyMap()) // 🔹 Nuevo dataset
    val totalTimeData: StateFlow<Map<String, Float>> = _totalTimeData.asStateFlow()

    init {
        viewModelScope.launch {
            filter.collect { selectedFilter ->
                // ✅ Se ejecuta solo cuando cambia el filtro
                loadSessions(selectedFilter)
            }
        }
    }

    fun loadSessions(filter: String) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val calendar = Calendar.getInstance()
        val endDate = dateFormat.format(calendar.time)

        val startDate = when (filter) {
            "Daily" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                dateFormat.format(calendar.time)
            }
            "Weekly" -> {
                calendar.add(Calendar.DAY_OF_YEAR, -7)
                dateFormat.format(calendar.time)
            }
            "Monthly" -> {
                calendar.add(Calendar.MONTH, -1)
                dateFormat.format(calendar.time)
            }
            else -> endDate
        }

        Log.d("HomeViewModel", "🔍 Buscando sesiones entre: $startDate y $endDate")

        viewModelScope.launch {
            sessionDao.getSessionsBetweenDatesFlow(startDate, endDate).collect { sessions ->
                Log.d("HomeViewModel", "📊 Sesiones recibidas de Room: $sessions")
                processSessions(filter, sessions)
            }
        }
    }

    private fun processSessions(filter: String, sessions: List<SessionEntity>) {
        Log.d("DailyChart", "📊 TotalTimeData (Daily): $_totalTimeData")

        when (filter) {
            "Daily" -> {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val sessionsToday = sessions.filter { it.date.startsWith(today) }

                val totalSessions = sessionsToday.size.toFloat() // 🔹 Cantidad de sesiones
                val totalMinutes = sessionsToday.sumOf { convertDurationToMinutes(it.duration) } // 🔹 Total minutos

                _sessionsData.value = mapOf(today to totalSessions) // 🔹 Sesiones por día
                _totalTimeData.value = mapOf(today to totalMinutes.toFloat()) // 🔹 Minutos totales
                _xLabels.value = listOf(today)
            }

            "Weekly" -> {
                val weekDays = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                val sessionsByDay = weekDays.associateWith { 0f }.toMutableMap()
                val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))

                sessions.forEach { session ->
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(session.date)
                    val dayName = dayFormat.format(parsedDate!!).replaceFirstChar { it.uppercase() }

                    Timber.tag("HomeViewModel").d("📅 Día procesado: $dayName con duración: ${session.duration}")

                    if (weekDays.contains(dayName)) {
                        val durationMinutes = convertDurationToMinutes(session.duration)
                        sessionsByDay[dayName] = ((sessionsByDay[dayName] ?: 0f) + durationMinutes).toFloat()
                    }
                }

                Timber.tag("HomeViewModel").d("📊 Weekly después de procesar: $sessionsByDay")

                _sessionsData.value = sessionsByDay
                _xLabels.value = weekDays
            }

            "Monthly" -> {
                val calendarFormat = SimpleDateFormat("d", Locale.getDefault())
                val sessionsByDay = mutableMapOf<Int, Float>()
                val daysInMonth = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)

                for (i in 1..daysInMonth) {
                    sessionsByDay[i] = 0f
                }

                sessions.forEach { session ->
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(session.date)
                    val dayOfMonth = calendarFormat.format(parsedDate!!).toInt()
                    val durationMinutes = convertDurationToMinutes(session.duration)

                    sessionsByDay[dayOfMonth] = ((sessionsByDay[dayOfMonth] ?: 0f) + durationMinutes).toFloat()
                }

                val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                if (!sessionsByDay.containsKey(today)) {
                    sessionsByDay[today] = 0f
                }

                val sortedKeys = sessionsByDay.keys.sorted()

                Log.d("HomeViewModel", "📊 Monthly después de procesar: $sessionsByDay")

                _sessionsData.value = sessionsByDay.mapKeys { it.key.toString() }
                _xLabels.value = sortedKeys.map { it.toString() }
            }
        }
    }
}

fun convertDurationToMinutes(duration: String): Double {
    val parts = duration.split(":")
    if (parts.size != 2) return 0.0

    val minutes = parts[0].toDoubleOrNull() ?: 0.0
    val seconds = parts[1].toDoubleOrNull() ?: 0.0

    return minutes + (seconds / 60.0) // ✅ Ahora devuelve `Double`
}
