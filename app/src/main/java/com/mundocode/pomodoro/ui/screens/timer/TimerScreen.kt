package com.mundocode.pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.mundocode.pomodoro.model.local.Timer
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.theme.PomodoroTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController, timer: Timer, viewModel: TimerViewModel = hiltViewModel()) {
    val timeState by viewModel.timerState.collectAsStateWithLifecycle()
//    val progress = timeState.remainingTime / (25 * 60 * 1000f)
//
//    val workDurationInput = remember {
//        mutableStateOf((timeState.workDuration / (60 * 1000)).toString())
//    }
//    val restDurationInput = remember {
//        mutableStateOf((timeState.breakDuration / (60 * 1000)).toString())
//    }

    LaunchedEffect(true) {
        viewModel.setupTimer(timer)
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
            startTimer = viewModel::startTimer,
            stopTimer = viewModel::stopTimer,
            resetTimer = viewModel::resetTimer,
        )
    }
}

@Composable
fun TimerContent(
    timerState: TimerState,
    modifier: Modifier = Modifier,
    startTimer: () -> Unit = {},
    stopTimer: () -> Unit = {},
    resetTimer: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
    ) {
//        Column(modifier = Modifier.padding(8.dp)) {
//            Text(
//                text = "Nombre de la sesión",
//                fontSize = 32.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(8.dp),
//            )
//        }
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { timerState.remainingTime / (25 * 60 * 1000f) },
                modifier = Modifier
                    .size(200.dp),
                color = if (timerState.isWorking) Color(0xFFB51C1C) else Color(0xFF03A9F4),
                strokeWidth = 10.dp,
                trackColor = ProgressIndicatorDefaults.circularColor,
            )
            Text(
                text = "${timerState.remainingTime / 1000 / 60}:${
                    (timerState.remainingTime / 1000 % 60).toString()
                        .padStart(2, '0')
                }",
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = when {
                timerState.isRunning -> "Trabajando"
                timerState.isWorking -> "Descanso"
                else -> "Detenido"
            },
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSecondary,
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
                    if (timerState.isRunning) {
                        stopTimer()
                    } else {
                        startTimer
                    }
                },
                colors = ButtonDefaults.buttonColors().copy(containerColor = MaterialTheme.colorScheme.onPrimary),
            ) {
                Icon(
                    painter = painterResource(
                        id = if (timerState.isRunning) R.drawable.pause else R.drawable.timer_play,
                    ),
                    contentDescription = if (timerState.isRunning) "Pausar" else "Reanudar",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
            Button(
                onClick = stopTimer,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4)),
            ) {
                Icon(
                    painter = painterResource(
                        id = R.drawable.timer_stop,
                    ),
                    contentDescription = "Detener",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp),
                )
            }
            Button(
                onClick = resetTimer,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63)),
            ) {
                Icon(Icons.Filled.Refresh, contentDescription = "Reiniciar", tint = Color.White)
            }
        }
//        OutlinedTextField(
//            value = workDurationInput.value,
//            onValueChange = { newValue ->
//                if (newValue.all { it.isDigit() }) {
//                    workDurationInput.value = newValue
//                    val newDuration = newValue.toLongOrNull() ?: (timerState.workDuration / (60 * 1000))
//                    viewModel.updateWorkDuration(newDuration * 60 * 1000L)
//                }
//            },
//            label = { Text("Duración Trabajo (min)") },
//            singleLine = true,
//            modifier = Modifier.padding(horizontal = 16.dp),
//            colors = TextFieldDefaults.colors(
//                focusedPlaceholderColor = Color(0xFF03A9F4),
//                unfocusedPlaceholderColor = Color(0xFF03A9F4),
//            ),
//        )
//        OutlinedTextField(
//            value = restDurationInput.value,
//            onValueChange = { newValue ->
//                if (newValue.all { it.isDigit() }) {
//                    restDurationInput.value = newValue
//                    val newDuration = newValue.toLongOrNull() ?: timerState.breakDuration / (60 * 1000)
//                    viewModel.updateBreakDuration(newDuration * 60 * 1000L) // Convertir a milisegundos
//                }
//            },
//            label = { Text("Duración Descanso (min)") },
//            singleLine = true,
//            modifier = Modifier.padding(horizontal = 16.dp),
//            colors = TextFieldDefaults.colors(
//                focusedPlaceholderColor = Color(0xFF03A9F4),
//                unfocusedPlaceholderColor = Color(0xFF03A9F4),
//            ),
//        )
    }
}

@Preview
@Composable
fun TimerScreenPreview() {
    PomodoroTheme {
        TimerScreen(
            timer = Timer(
                sessionName = "Sesión 1",
                mode = "Trabajo",
                timer = "1:00",
                pause = "1:00",
            ),
            navController = NavController(LocalContext.current),
        )
    }
}
