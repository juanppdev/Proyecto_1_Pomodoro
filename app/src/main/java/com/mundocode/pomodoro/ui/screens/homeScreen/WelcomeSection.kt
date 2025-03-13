package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseUser

@Composable
fun WelcomeSection(user: FirebaseUser?) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Bienvenid@, ${user?.displayName ?: "Usuario"}",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "¿Qué quieres hacer hoy?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
