package com.mundocode.pomodoro.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

//private val DarkColorScheme = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,
//)

//private val LightColorScheme = lightColorScheme(
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
//)

@Composable
fun PomodoroTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
//    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = DarkColors.primary,
            background = DarkColors.background,
            secondary = DarkColors.secondary,
            surface = DarkColors.secondaryBackground,
            onPrimary = DarkColors.backgroundButtons,
            onBackground = DarkColors.darkGrayColor,
            onSecondary = DarkColors.text,
            onTertiary = DarkColors.stadisticsButton
        )
    } else {
        lightColorScheme(
            primary = LightColors.primary,
            background = LightColors.background,
            secondary = LightColors.secondary,
            surface = LightColors.secondaryBackground,
            onPrimary = LightColors.backgroundButtons,
            onBackground = LightColors.darkGrayColor,
            onSecondary = LightColors.text,
            onTertiary = LightColors.stadisticsButton
        )
    }
    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content,
    )
}
