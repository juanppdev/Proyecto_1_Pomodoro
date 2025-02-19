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

    init {
        viewModelScope.launch {
            filter.collect { selectedFilter ->
                // ✅ Se ejecuta solo cuando cambia el filtro
                loadSessions(selectedFilter)
            }
        }
    }

    fun setFilter(newFilter: String) {
        if (_filter.value != newFilter) { // ✅ Solo cambiar si es diferente
            _filter.value = newFilter
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

        viewModelScope.launch {
            sessionDao.getSessionsBetweenDatesFlow(startDate, endDate).collect { sessions ->
                // ✅ Se actualiza en tiempo real
                processSessions(filter, sessions)
            }
        }
    }

    private fun processSessions(filter: String, sessions: List<SessionEntity>) {
        when (filter) {
            "Daily" -> {
                val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val sessionsToday = sessions.filter { it.date.startsWith(today) }

                val totalSeconds = sessionsToday.sumOf { it.duration.toIntOrNull() ?: 0 }
                val totalMinutes = totalSeconds / 60f

                Log.d("HomeViewModel", "📊 Daily: $today -> $totalMinutes minutos")

                _sessionsData.value = mapOf(today to totalMinutes)
                _xLabels.value = listOf(today)
            }

            "Weekly" -> {
                val weekDays = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
                val sessionsByDay = weekDays.associateWith { 0f }.toMutableMap()
                val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES"))

                sessions.forEach { session ->
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(session.date)
                    val dayName = dayFormat.format(parsedDate!!).capitalize(Locale.ROOT)

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

                for (i in 1..Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    sessionsByDay[i.toString()] = 0f
                }

                sessions.forEach { session ->
                    val parsedDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(session.date)
                    val dayOfMonth = calendarFormat.format(parsedDate!!)
                    val durationMinutes = convertDurationToMinutes(session.duration)

                    Log.d(
                        "HomeViewModel",
                        "📅 Día $dayOfMonth procesado con duración: ${session.duration} -> $durationMinutes minutos",
                    )

                    sessionsByDay[dayOfMonth] = (sessionsByDay[dayOfMonth] ?: 0f) + durationMinutes
                }

                val today = Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
                if (!sessionsByDay.containsKey(today)) {
                    sessionsByDay[today] = 0f
                }

                Log.d("HomeViewModel", "📊 Monthly después de procesar: $sessionsByDay")

                _sessionsData.value = sessionsByDay
                _xLabels.value = sessionsByDay.keys.toList()
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
