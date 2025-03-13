package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StatsSection(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    sessionsData: Map<String, Float>,
    xLabels: List<String>,
    totalTime: Map<String, Float> = emptyMap(),
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Dropdown para seleccionar la vista
        Box {
            OutlinedButton(
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                onClick = { expanded = true },
            ) {
                Row {
                    Text(selectedOption, color = MaterialTheme.colorScheme.onSurface)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown")
                }
            }
            DropdownMenu(
                modifier = Modifier.padding(10.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                listOf("Daily", "Weekly", "Monthly").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            if (selectedOption != option) {
                                onOptionSelected(option)
                            }
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el gráfico según la opción seleccionada
        when (selectedOption) {
            "Daily" -> DailyChart(sessionsData = sessionsData, timeData = totalTime, xLabels = xLabels)
            "Weekly" -> WeeklyChart(sessionsData, xLabels)
            "Monthly" -> ProductivityCalendar(sessionsData)
        }
    }
}
