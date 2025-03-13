package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun OptionsSection(navController: NavController) {
    Column(modifier = Modifier.padding(8.dp)) {
        OptionButtons(
            color = MaterialTheme.colorScheme.primary,
            textButton = "Empezar\nPomodoro",
            icon = R.drawable.timer_icon,
            descriptionIcon = "botón de Empezar Pomodoro",
            onClick = { navController.kiwiNavigation(Destinations.SetupSession) },
        )

        OptionButtons(
            color = MaterialTheme.colorScheme.secondary,
            textButton = "Ver\nHábitos",
            icon = R.drawable.habit_icon,
            descriptionIcon = "botón Ver Hábitos",
            onClick = { navController.kiwiNavigation(Destinations.Habits) },
        )

        OptionButtons(
            color = MaterialTheme.colorScheme.tertiary,
            textButton = "Ver\nTareas",
            icon = R.drawable.checklist_icon,
            descriptionIcon = "botón Ver Tareas",
            onClick = { navController.kiwiNavigation(Destinations.Task) },
        )
    }
}
