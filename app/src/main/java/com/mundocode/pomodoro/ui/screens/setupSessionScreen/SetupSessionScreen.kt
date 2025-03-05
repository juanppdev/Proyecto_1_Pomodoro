package com.mundocode.pomodoro.ui.screens.setupSessionScreen

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import com.mundocode.pomodoro.ui.screens.SharedPointsViewModel
import com.mundocode.pomodoro.ui.screens.points.PointsViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigate

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSerializationApi::class)
@Composable
fun SetupSessionScreen(
    navController: NavController,
    viewModel: SetupSessionViewModel = hiltViewModel(),
    pointsViewModel: PointsViewModel = hiltViewModel(),
    sharedPointsViewModel: SharedPointsViewModel = hiltViewModel(),
) {
    val state by viewModel.sessionState.collectAsStateWithLifecycle()
    val user = Firebase.auth.currentUser
    val userPoints by sharedPointsViewModel.userPoints.collectAsState()

    LaunchedEffect(Unit) {
        pointsViewModel.loadUserPoints(user?.displayName.toString())
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = "Configurar sesión Pomodoro",
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
                texto = "Puntos: $userPoints",
            )
        },
    ) { padding ->

        SetupSessionContent(
            state = state,
            modifier = Modifier.padding(padding),
            updateSessionName = viewModel::updateSessionName,
            startSession = {
                val timerJson = state.timer.toJson() // Convertir a JSON
                navController.currentBackStackEntry?.savedStateHandle?.set("timer", timerJson)
                navController.kiwiNavigate(Destinations.TimerScreen(state.timer))
            },

        )
    }
}

@Composable
fun SetupSessionContent(
    state: SessionState,
    modifier: Modifier = Modifier,
    startSession: () -> Unit = {},
    updateSessionName: (String) -> Unit = {},
    viewModel: SetupSessionViewModel = hiltViewModel(),
) {
    var showTimerPicker by remember { mutableStateOf(false) }
    var showPausePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF7F7F7)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Descripción de la Sesión",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF333333),
        )
        OutlinedTextField(
            value = state.timer.sessionName,
            onValueChange = updateSessionName,
            shape = RoundedCornerShape(18.dp),
            label = { Text("Nombre de la sesión") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Temporizador Personalizado",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333),
            )
            Button(
                onClick = { showTimerPicker = true },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text(state.timer.timer, color = Color.White)
            }
            if (showTimerPicker) {
                TimePickerDialogT(
                    title = "Selecciona el temporizador",
                    initialTime = state.timer.timer,
                    onTimeSelected = { viewModel.updateTimer(it) },
                    onDismiss = { showTimerPicker = false },
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "Temporizador de Pausa",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333),
            )
            Button(
                onClick = { showPausePicker = true },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) {
                Text(state.timer.pause, color = Color.White)
            }
            if (showPausePicker) {
                TimePickerDialogT(
                    title = "Selecciona el tiempo de pausa",
                    initialTime = state.timer.pause,
                    onTimeSelected = { viewModel.updatePause(it) },
                    onDismiss = { showPausePicker = false },
                )
            }
        }

        Button(
            onClick = startSession,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Text(
                text = "Iniciar Sesión", // TODO: Extract string
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
            )
        }
    }
}

@Composable
fun TimePickerDialogT(title: String, initialTime: String, onTimeSelected: (String) -> Unit, onDismiss: () -> Unit) {
    val context = LocalContext.current
    val timeParts = initialTime.split(":").map { it.toIntOrNull() ?: 0 }
    val initialHour = timeParts[0]
    val initialMinute = timeParts[1]

    LaunchedEffect(Unit) {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val formattedTime = "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                onTimeSelected(formattedTime)
                onDismiss() // Cerrar diálogo después de la selección
            },
            initialHour,
            initialMinute,
            true,
        ).show()
    }
}

@Preview(showBackground = true)
@Composable
fun SetupSessionScreenPreview() {
    SetupSessionContent(
        state = SessionState(),
    )
}
