package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ProductivityCalendar(sessionsData: Map<String, Float>) {
    val today = Calendar.getInstance()
    val daysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH)
    val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())

    val firstDayOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }.get(Calendar.DAY_OF_WEEK) - 1

    val dayList = List(firstDayOfMonth) { null } + (1..daysInMonth).toList()

    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var selectedSessionTime by remember { mutableStateOf<Float?>(null) }
    var showPopup by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Text(
                text = monthYearFormat.format(today.time).replaceFirstChar { it.uppercase() },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("L", "M", "X", "J", "V", "S", "D").forEach {
                Text(text = it, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyVerticalGrid(
            modifier = Modifier.height(300.dp),
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(vertical = 8.dp),
        ) {
            items(dayList.size) { index ->
                val day = dayList[index]
                val sessionTime = day?.let { sessionsData.getOrDefault(it.toString(), 0f) } ?: 0f

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                day == null -> Color.Transparent
                                day == today.get(Calendar.DAY_OF_MONTH) -> Color(0xFF03A9F4) // Azul día actual
                                sessionTime == 0f -> Color.Gray // Sin sesión
                                sessionTime in 0f..1f -> Color.Magenta // 🔸 Sesión muy corta (menos de 1 min)
                                sessionTime in 1f..29f -> Color.Red // 🔴 Baja productividad
                                sessionTime in 30f..59f -> Color.Yellow // 🟡 Media productividad
                                sessionTime >= 60f -> Color.Green // 🟢 Alta productividad
                                else -> Color.Gray
                            },
                        )
                        .clickable(enabled = day != null && sessionTime > 0) {
                            selectedDay = day
                            selectedSessionTime = sessionTime
                            showPopup = true
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (day != null) {
                        Text(
                            text = day.toString(),
                            color = if (day == today.get(Calendar.DAY_OF_MONTH)) Color.White else Color.Black,
                            fontWeight = if (day ==
                                today.get(Calendar.DAY_OF_MONTH)
                            ) {
                                FontWeight.Bold
                            } else {
                                FontWeight.Normal
                            },
                        )
                    }
                }
            }
        }
    }

    if (showPopup) {
        AlertDialog(
            onDismissRequest = { showPopup = false },
            title = { Text("Sesiones del día $selectedDay", color = MaterialTheme.colorScheme.onSurface) },
            text = {
                val totalMinutes = selectedSessionTime!!.toInt()
                val totalSeconds = ((selectedSessionTime!! - totalMinutes) * 60).toInt()

                Text(
                    "Tiempo total: $totalMinutes min $totalSeconds seg",
                    color = MaterialTheme.colorScheme.onSurface,
                )
            },
            confirmButton = {
                Button(onClick = { showPopup = false }) { Text("Aceptar") }
            },
        )
    }
}
