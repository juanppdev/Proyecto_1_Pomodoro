package com.mundocode.pomodoro.ui.screens.setupSessionScreen

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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupSessionScreen(viewModel: SetupSessionViewModel = hiltViewModel(), navigateTo: (Destinations) -> Unit = {}) {
    val state by viewModel.sessionState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar Sesión Pomodoro") }, // TODO: Extract string
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD9D9D9), // TODO: Use theme color instead of this harcoded one
                    titleContentColor = Color.Black,
                ),
            )
        },
    ) { padding ->

        SetupSessionContent(
            state = state,
            modifier = Modifier.padding(padding),
            updateSessionName = viewModel::updateSessionName,
            updateMode = viewModel::updateMode,
            updateTimer = viewModel::updateTimer,
            updatePause = viewModel::updatePause,
            startSession = {
                navigateTo(Destinations.TimerScreen(state.timer))
            },
        )
    }
}

@Composable
fun SetupSessionContent(
    state: SessionState,
    modifier: Modifier = Modifier,
    updateSessionName: (String) -> Unit = {},
    updateMode: (String) -> Unit = {},
    updateTimer: (String) -> Unit = {},
    updatePause: (String) -> Unit = {},
    startSession: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF7F7F7)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
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
        Text(
            text = "Tiempo",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF333333),
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            listOf("25:00", "30:00", "60:00").forEach { time ->
                Button(
                    onClick = { /* Handle click */ },
                    shape = RoundedCornerShape(50),
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF192229),
                    ),
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = time,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
        OutlinedTextField(
            value = state.timer.timer,
            onValueChange = updateTimer,
            shape = MaterialTheme.shapes.medium,
            label = { Text("Temporizador personalizado (mm:ss)") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(painterResource(id = R.drawable.timer), contentDescription = null) },
        )
        OutlinedTextField(
            value = state.timer.mode,
            onValueChange = updateMode,
            shape = MaterialTheme.shapes.medium,
            label = { Text("Modo actual") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
        )
        OutlinedTextField(
            value = state.timer.pause,
            onValueChange = updatePause,
            shape = RoundedCornerShape(18.dp),
            label = { Text("Temporizador de pausa (mm:ss)") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(painterResource(id = R.drawable.pause), contentDescription = null) },
        )
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

@Preview(showBackground = true)
@Composable
fun SetupSessionScreenPreview() {
    SetupSessionContent(
        state = SessionState(),
    )
}
