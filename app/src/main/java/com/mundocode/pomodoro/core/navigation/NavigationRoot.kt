package com.mundocode.pomodoro.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mundocode.pomodoro.ui.screens.homeScreen.HomeScreen
import com.mundocode.pomodoro.ui.screens.loginScreen.LoginScreen
import com.mundocode.pomodoro.ui.screens.setupSessionScreen.SetupSessionScreen
import com.mundocode.pomodoro.ui.screens.timer.TimerScreen

@Composable
fun NavigationRoot(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Login,
        modifier = modifier.fillMaxSize(),
    ) {
        composable<Destinations.Login> {
            LoginScreen(
                navController = navController,
            )
        }
        composable<Destinations.Home> {
            HomeScreen(
                navigateTo = { destination ->
                    navController.navigate(destination)
                },
            )
        }
        composable<Destinations.SetupSession> {
            SetupSessionScreen()
        }
        composable<Destinations.Timer> {
            TimerScreen(navController = navController)
        }
    }
}
