package com.mundocode.pomodoro.ui.screens.homeScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.sessionDb.SessionDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val sessionDao: SessionDao) : ViewModel() {

    private val _sessionsData = MutableStateFlow<Map<String, Float>>(emptyMap())
    val sessionsData: StateFlow<Map<String, Float>> = _sessionsData.asStateFlow()

    private val _xLabels = MutableStateFlow<List<String>>(emptyList())
    val xLabels: StateFlow<List<String>> = _xLabels.asStateFlow()

    init {
        loadSessions("Weekly") // ✅ Mostrar por defecto la vista semanal
    }

    fun loadSessions(filter: String) {
        viewModelScope.launch {
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

            val sessions = sessionDao.getSessionsBetweenDates(startDate, endDate)
            Log.d("HomeViewModel", "✅ Sesiones recuperadas ($filter): $sessions")

            if (sessions.isEmpty()) {
                Log.d("HomeViewModel", "⚠️ No se encontraron sesiones en Room.")
            }

            when (filter) {
                "Weekly" -> {
                    val weekDays = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                    val sessionsByDay = weekDays.associateWith { 0f }.toMutableMap()
                    val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))

                    sessions.forEach { session ->
                        val parsedDate = dateFormat.parse(session.date)
                        val calendarInstance = Calendar.getInstance().apply { time = parsedDate!! }
                        val dayName = dayFormat.format(calendarInstance.time).capitalize(Locale.ROOT)

                        Log.d("HomeViewModel", "📅 Día procesado: $dayName con duración: ${session.duration}")

                        if (weekDays.contains(dayName)) {
                            val durationMinutes = convertDurationToMinutes(session.duration)
                            sessionsByDay[dayName] = (sessionsByDay[dayName] ?: 0f) + durationMinutes
                        } else {
                            Log.d("HomeViewModel", "⚠️ Día ignorado: $dayName no está en la lista de días válidos.")
                        }
                    }

                    Log.d("HomeViewModel", "📊 Weekly después de procesar: $sessionsByDay")

                    _sessionsData.value = sessionsByDay
                    _xLabels.value = weekDays
                }
                "Monthly" -> {
                    val calendarFormat = SimpleDateFormat("dd", Locale.getDefault())
                    val sessionsByDay = mutableMapOf<String, Float>()

                    for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        sessionsByDay[i.toString()] = 0f
                    }

                    sessions.forEach { session ->
                        val dayOfMonth = calendarFormat.format(dateFormat.parse(session.date)!!)
                        val durationMinutes = convertDurationToMinutes(session.duration)

                        Log.d(
                            "HomeViewModel",
                            "📅 Día procesado: $dayOfMonth con duración: ${session.duration} -> $durationMinutes minutos",
                        )

                        sessionsByDay[dayOfMonth] = (sessionsByDay[dayOfMonth] ?: 0f) + durationMinutes
                    }

                    val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
                    if (!sessionsByDay.containsKey(today)) {
                        sessionsByDay[today] = 0f // ✅ Asegurar que el día actual está incluido
                    }

                    Log.d("HomeViewModel", "📊 Monthly después de procesar: $sessionsByDay")

                    _sessionsData.value = sessionsByDay
                    _xLabels.value = sessionsByDay.keys.toList()
                }
            }
        }
    }
}

fun convertDurationToMinutes(duration: String): Float {
    val parts = duration.split(":")
    if (parts.size != 2) return 0f

    val minutes = parts[0].toFloatOrNull() ?: 0f
    val seconds = parts[1].toFloatOrNull() ?: 0f

    return minutes + (seconds / 60f) // ✅ Convertir segundos a minutos
}
