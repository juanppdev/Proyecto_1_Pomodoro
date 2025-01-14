package com.mundocode.proyecto_1_pomodoro.ui.screens.setupSessionScreen

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mundocode.proyecto_1_pomodoro.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupSessionScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configurar Sesión Pomodoro") },
                colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFD9D9D9),
                    titleContentColor = Color.Black,
                ),
            )
        },
    ) { padding ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
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
                value = "",
                onValueChange = {},
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
                value = "",
                onValueChange = {},
                shape = RoundedCornerShape(18.dp),
                label = { Text("Temporizador personalizado (mm:ss)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(painterResource(id = R.drawable.timer), contentDescription = null) },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                shape = RoundedCornerShape(18.dp),
                label = { Text("Modo actual") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
            )
            OutlinedTextField(
                value = "",
                onValueChange = {},
                shape = RoundedCornerShape(18.dp),
                label = { Text("Temporizador de pausa (mm:ss)") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(painterResource(id = R.drawable.pause), contentDescription = null) },
            )
            Button(
                onClick = { /* Start session */ },
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFB51C1C),
                ),
            ) {
                Text(
                    text = "Iniciar Sesión",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
fun SetupSessionScreenPreview() {
    SetupSessionScreen()
}
