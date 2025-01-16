package com.mundocode.pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.ui.viewmodel.TimerViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(viewModel: TimerViewModel = hiltViewModel(), navController: NavController) {
    // Usamos .value directamente, ya que timerState es un State, no un Flow
    val timeState = viewModel.timerState.value
    val progress = timeState.remainingTime / (25 * 60 * 1000f)

    val workDurationInput = remember {
        mutableStateOf((timeState.workDuration / (60 * 1000)).toString())
    }
    val restDurationInput = remember {
        mutableStateOf((timeState.breakDuration / (60 * 1000)).toString())
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Localized description"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
            )
        },
    ) { paddingValues ->
        // El resto del código sigue igual, solo se modifica la forma de obtener el estado
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
                    text = "${timeState.remainingTime / 1000 / 60}:${
                        (timeState.remainingTime / 1000 % 60).toString()
                            .padStart(2, '0')
                    }",
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFC107))
                ) {
                    Text(text = if (timeState.isRunning) "Pausar" else "Reanudar", color = Color.White)
                }
                Button(
                    onClick = { viewModel.stopTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                ) {
                    Text(text = "Detener", color = Color.White)
                }
            }
            OutlinedTextField(
                value = workDurationInput.value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        workDurationInput.value = newValue
                        val newDuration = newValue.toLongOrNull() ?: (timeState.workDuration / (60 * 1000))
                        viewModel.updateWorkDuration(newDuration * 60 * 1000L)
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
            OutlinedTextField(
                value = restDurationInput.value,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        restDurationInput.value = newValue
                        val newDuration = newValue.toLongOrNull() ?: timeState.breakDuration / (60 * 1000)
                        viewModel.updateBreakDuration(newDuration * 60 * 1000L) // Convertir a milisegundos
                    }
                },
                label = { Text("Duración Descanso (min)") },
                singleLine = true,
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedPlaceholderColor = Color(0xFF03A9F4),
                    unfocusedPlaceholderColor = Color(0xFF03A9F4)
                )
            )
        }
    }
}

