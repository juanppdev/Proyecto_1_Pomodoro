package com.mundocode.pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = Color(0xFFEFEFEF)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        ) {
            Column(modifier = Modifier.padding(8.dp)) {

                Text(
                    text = "Nombre de la sesión",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp),
                )
            }
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .size(200.dp),
                    color = if (timeState.isWorking) Color(0xFFB51C1C) else Color(0xFF03A9F4),
                    strokeWidth = 10.dp,
                    trackColor = ProgressIndicatorDefaults.circularTrackColor,
                )
                Text(
                    text = "${timeState.remainingTime / 1000 / 60}:${
                        (timeState.remainingTime / 1000 % 60).toString()
                            .padStart(2, '0')
                    }",
                    textAlign = TextAlign.Center,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = when {
                    timeState.isRunning -> "Trabajando"
                    timeState.isWorking -> "Descanso"
                    else -> "Detenido"
                },
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
                    onClick = {
                        if (timeState.isRunning) {
                        viewModel.stopTimer()
                    } else {
                        viewModel.startTimer()
                    } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4E21))
                ) {
                    Icon(
                        painter = painterResource(
                            id = if (timeState.isRunning) R.drawable.pause else R.drawable.timer_play
                        ),
                        contentDescription = if (timeState.isRunning) "Pausar" else "Reanudar",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )

                }
                Button(
                    onClick = { viewModel.stopTimer() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
                ) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.timer_stop
                        ),
                        contentDescription = "Detener",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
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

