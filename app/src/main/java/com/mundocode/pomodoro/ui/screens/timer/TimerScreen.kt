package com.mundocode.pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.model.local.Timer
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.theme.PomodoroTheme
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigate

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun TimerScreen(navController: NavController, viewModel: TimerViewModel = hiltViewModel()) {
    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val timerJson = savedStateHandle?.get<String>("timer") ?: ""
    val timer = if (timerJson.isNotEmpty()) Timer.fromJson(timerJson) else Timer()
    val context = LocalContext.current // ✅ Obtener `context`

    LaunchedEffect(timer) {
        viewModel.setupTimer(timer)
    }

    val timeState by viewModel.timerState.collectAsStateWithLifecycle()
    val sessionHistory by viewModel.sessionHistory.collectAsStateWithLifecycle()
    val navigateToHome by viewModel.navigateToHome.collectAsStateWithLifecycle()

    if (navigateToHome) {
        AlertDialog(
            onDismissRequest = { viewModel.onPopupDismissed() },
            title = { Text("Sesión Finalizada") },
            text = {
                Column {
                    Text("Resumen de la sesión completada:")
                    sessionHistory.takeLast(2).forEach { session ->
                        Text("📌 ${session.type}: ${session.duration} min")
                        Text("📅 Fecha: ${session.date}")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.onPopupDismissed()
                    navController.kiwiNavigate(Destinations.HomeScreen)
                }) {
                    Text("Aceptar")
                }
            },
        )
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = timer.sessionName,
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Localized description",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        TimerContent(
            timerState = timeState,
            modifier = Modifier.padding(paddingValues),
            startTimer = { viewModel.startTimer(context) }, // ✅ Pasamos `context`
            stopTimer = viewModel::stopTimer,
            resetTimer = viewModel::resetTimer,
        )

        // ✅ Llamar onTimerFinished() con contexto cuando el temporizador llega a 0
        if (timeState.remainingTime == 0L) {
            viewModel.onTimerFinished(context)
        }
    }
}

@Composable
fun TimerContent(
    timerState: TimerState,
    modifier: Modifier = Modifier,
    startTimer: () -> Unit,
    stopTimer: () -> Unit,
    resetTimer: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { timerState.remainingTime / timerState.workDuration.toFloat() },
                modifier = Modifier.size(200.dp),
                color = if (timerState.isWorking) Color(0xFFB51C1C) else Color(0xFF03A9F4),
                strokeWidth = 10.dp,
            )
            Text(
                text = "${timerState.remainingTime / 1000 / 60}:${
                    (timerState.remainingTime / 1000 % 60).toString().padStart(2, '0')
                }",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { if (timerState.isRunning) stopTimer() else startTimer() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            ) {
                Icon(
                    painter = painterResource(
                        id = if (timerState.isRunning) R.drawable.pause else R.drawable.timer_play,
                    ),
                    contentDescription = if (timerState.isRunning) "Pausar" else "Reanudar",
                    tint = Color.White,
                )
            }
            Button(
                onClick = resetTimer,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar", tint = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun TimerScreenPreview() {
    PomodoroTheme {
        TimerScreen(
            navController = NavController(LocalContext.current),
            viewModel = hiltViewModel(),
        )
    }
}
