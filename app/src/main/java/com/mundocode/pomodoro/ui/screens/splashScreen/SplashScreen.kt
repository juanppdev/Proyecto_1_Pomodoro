package com.mundocode.pomodoro.ui.screens.splashScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.screens.loginScreen.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun SplashScreen(navController: NavController, viewModel: LoginViewModel = hiltViewModel()) {
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    var dots by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        while (true) {
            dots = when (dots) {
                "" -> "."
                "." -> ".."
                ".." -> "..."
                else -> ""
            }
            delay(500) // Cambia los puntos cada 500ms
        }
    }

    LaunchedEffect(loginSuccess) {
        delay(1000) // Retraso opcional para una mejor transición
        if (loginSuccess) {
            navController.kiwiNavigation(Destinations.HomeScreen) {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.kiwiNavigation(Destinations.Login) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Cargando$dots",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
        )
        // CircularProgressIndicator() // Muestra un loading mientras decide
    }
}
