package com.mundocode.pomodoro.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.mundocode.pomodoro.ui.screens.habits.HabitsScreen
import com.mundocode.pomodoro.ui.screens.homeScreen.HomeScreen
import com.mundocode.pomodoro.ui.screens.setupSessionScreen.SetupSessionScreen
import com.mundocode.pomodoro.ui.screens.tasks.TaskScreen
import com.mundocode.pomodoro.ui.screens.timer.TimerScreen
import kotlinx.serialization.ExperimentalSerializationApi
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigate

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = createRoutePattern<Destinations.HomeScreen>(),
        modifier = modifier.fillMaxSize(),
    ) {
        composable<Destinations.HomeScreen> {
            HomeScreen(
                navController = navController,
                navigateTo = { destination ->
                    navController.kiwiNavigate(destination)
                },
            )
        }
        composable<Destinations.SetupSessionScreen> {
            SetupSessionScreen(
                navController = navController,
                navigateTo = { destination ->
                    navController.kiwiNavigate(destination)
                },
            )
        }
        composable<Destinations.TimerScreen> {
            TimerScreen(navController = navController, timer = timer)
        }
        composable<Destinations.Task> {
            TaskScreen(navController = navController)
        }
        composable<Destinations.Habits> {
            HabitsScreen(navController = navController)
        }
    }
}
