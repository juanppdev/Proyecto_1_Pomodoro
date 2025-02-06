package com.mundocode.pomodoro.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.mundocode.pomodoro.ui.screens.homeScreen.HomeScreen
import com.mundocode.pomodoro.ui.screens.loginScreen.LoginScreen
import com.mundocode.pomodoro.ui.screens.setupSessionScreen.SetupSessionScreen
import com.mundocode.pomodoro.ui.screens.timer.TimerScreen
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = createRoutePattern<Destinations.Login>(),
    ) {
        composable<Destinations.Login> {
            LoginScreen(
                navController = navController,
            )
        }
        composable<Destinations.HomeScreen> {
            HomeScreen(navController = navController)
        }
        composable<Destinations.SetupSessionScreen> {
            SetupSessionScreen(
                navController = navController,
            )
        }
        composable<Destinations.TimerScreen> {
            TimerScreen(navController = navController, timer = timer)
        }
    }
}
