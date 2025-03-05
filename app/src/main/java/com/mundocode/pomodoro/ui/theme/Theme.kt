package com.mundocode.pomodoro.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

// private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,
// )

// private val LightColorScheme = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40,
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//     */
// )

@Composable
fun PomodoroTheme(themeViewModel: ThemeViewModel = hiltViewModel(), content: @Composable () -> Unit) {
    val selectedTheme by themeViewModel.selectedTheme.collectAsState()

    val colors = when (selectedTheme) {
        "Tema Oscuro" -> DarkColorScheme
        "Tema Azul" -> BlueColorScheme
        "Tema Rojo" -> RedColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
