package com.mundocode.pomodoro.ui.screens.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.model.local.Timer
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.screens.points.PointsViewModel
import com.mundocode.pomodoro.ui.screens.points.PointsViewModelFactoryProvider
import com.mundocode.pomodoro.ui.theme.PomodoroTheme
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigate

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun TimerScreen(
    navController: NavController,
    viewModel: TimerViewModel = hiltViewModel(),
    factoryProvider: PointsViewModelFactoryProvider = hiltViewModel(),
) {
    val user = Firebase.auth.currentUser
    val userId = Firebase.auth.currentUser?.uid ?: ""

    // Crear el ViewModel usando la factory del provider
    val pointsViewModel: PointsViewModel = viewModel(
        factory = PointsViewModel.provideFactory(
            assistedFactory = factoryProvider.pointsViewModelFactory,
            userId = userId,
        ),
    )

    val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
    val timerJson = savedStateHandle?.get<String>("timer") ?: ""
    val timer = if (timerJson.isNotEmpty()) {
        Timer.fromJson(timerJson)
    } else {
        Timer(
            sessionName = "Session",
            mode = "Work",
            timer = "25",
            pause = "5",
        ) // ✅ Usar fromJson()
    }
    val context = LocalContext.current // ✅ Obtener `context`
    val userPoints by pointsViewModel.userPoints.collectAsState()

//    LaunchedEffect(Unit) {
//        pointsViewModel.loadUserPoints(user?.displayName.toString())
//    }

    LaunchedEffect(timer) {
        viewModel.setupTimer(timer)
    }

    val sessionHistory by viewModel.sessionHistory.collectAsStateWithLifecycle()
    val navigateToHome by viewModel.navigateToHome.collectAsStateWithLifecycle()

    val isPomodoroComplete by viewModel.isPomodoroComplete.collectAsState()
    val showAnimation by viewModel.showAnimation.collectAsState()

    if (navigateToHome) {
        AlertDialog(
            onDismissRequest = { viewModel.onPopupDismissed() },
            title = { Text("Sesión Finalizada", color = MaterialTheme.colorScheme.inverseSurface) },
            text = {
                Column {
                    Text("Resumen de la sesión completada:", color = MaterialTheme.colorScheme.inverseSurface)
                    sessionHistory.forEach { session ->
                        Text(
                            "📌 ${session.type}: ${session.duration} min",
                            color = MaterialTheme.colorScheme.inverseSurface,
                        )
                        Text("📅 Fecha: ${session.date}", color = MaterialTheme.colorScheme.inverseSurface)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onPopupDismissed()
                        navController.kiwiNavigate(Destinations.HomeScreen)
                    },
                ) {
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
                image = user?.photoUrl.toString(),
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = "Localized description",
                        )
                    }
                },
                texto = "puntos: $userPoints",
            )
        },
    ) { paddingValues ->

        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (showAnimation && isPomodoroComplete) {
                LottieAnimationView { viewModel.hideAnimation() }
            } else {
                TimerContent(
                    timerState = viewModel.timerState.collectAsState().value,
                    startTimer = { viewModel.startTimer(context) },
                    stopTimer = { viewModel.stopTimer(context) },
                    resetTimer = { viewModel.resetTimer(context) },
                )
            }
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

@Composable
fun LottieAnimationView(onAnimationEnd: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.congratulations))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
    )

    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onAnimationEnd()
        }
    }

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier.fillMaxWidth().height(250.dp),
    )
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
