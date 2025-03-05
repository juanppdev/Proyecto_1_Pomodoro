package com.mundocode.pomodoro.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE), // Morado vibrante
    secondary = Color(0xFF03DAC5), // Verde agua brillante
    onSecondary = Color(0xFF000000), // Texto negro sobre el secundario
    background = Color(0xFFF5F5F5), // Gris muy claro en lugar de blanco puro
    surface = Color(0xFFFFFFFF), // Blanco puro para mejor contraste
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // Morado claro, más relajante para la vista
    secondary = Color(0xFF03DAC5), // Verde agua
    background = Color(0xFF1E1E1E), // Gris oscuro para menos fatiga visual
    surface = Color(0xFF2C2C2C), // Superficie ligeramente más clara que el fondo
)

val BlueColorScheme = lightColorScheme(
    primary = Color(0xFF1976D2), // Azul fuerte y profundo
    secondary = Color(0xFF03A9F4), // Azul claro para resaltar elementos
    background = Color(0xFFE3F2FD), // Azul muy claro como base
    surface = Color(0xFF90CAF9), // Azul intermedio para mejor diferenciación
)

val RedColorScheme = lightColorScheme(
    primary = Color(0xFFD32F2F), // Rojo fuerte pero no tan vibrante
    secondary = Color(0xFFFF5252), // Rojo más claro para contrastar
    background = Color(0xFFFFEBEE), // Rosa muy claro como base
    surface = Color(0xFFFFCDD2), // Rojo pastel para mejor equilibrio
)
