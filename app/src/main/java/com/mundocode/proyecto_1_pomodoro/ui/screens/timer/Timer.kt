package com.mundocode.proyecto_1_pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mundocode.proyecto_1_pomodoro.R
import com.mundocode.proyecto_1_pomodoro.ui.viewmodel.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(viewModel: TimerViewModel, modifier: Modifier) {
    val timeState = viewModel.timerState.value
    val progress = timeState.remainingTime / (25 * 60 * 1000f)

    val workDurationInput = remember {
        mutableStateOf((timeState.workDuration / (60 * 1000)).toString())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(200.dp),
                    progress = progress,
                    color = if (timeState.isWorking) Color(0xFFFFC107) else Color(0xFF03A9F4),
                    strokeWidth = 10.dp,
                )
                Text(
                    text = "${timeState.remainingTime / 1000 / 60}:${(timeState.remainingTime / 1000 % 60).toString().padStart(
                        2,
                        '0',
                    )}",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    color = Color(0xFF0000FF),
                )
            }
            Text(
                text = if (timeState.isRunning) "Trabajando" else "Detenido",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                Button(
                    onClick = { viewModel.startTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107)),
                ) {
                    Text(text = "Iniciar", color = Color.White)
                }
                Button(
                    onClick = { viewModel.stopTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
                ) {
                    Text(text = "Detener", color = Color.White)
                }
                Button(
                    onClick = { viewModel.resetTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
                ) {
                    Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar", tint = Color.White)
                }
            }
            OutlinedTextField(
                value = workDurationInput.value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        workDurationInput.value = newValue
                        val newDuration = newValue.toLongOrNull() ?: timeState.workDuration / (60 * 1000)
                        viewModel.updateWorkDuration(newDuration * 60 * 1000L) // Convertir a milisegundos
                    }
                },
                label = { Text("Duración Trabajo (min)") },
                singleLine = true,
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = Color(0xFF03A9F4),
                    unfocusedPlaceholderColor = Color(0xFF03A9F4),
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    TimerScreen(viewModel = TimerViewModel(), modifier = Modifier)
}
