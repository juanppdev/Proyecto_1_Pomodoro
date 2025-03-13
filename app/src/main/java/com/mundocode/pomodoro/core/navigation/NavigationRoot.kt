package com.mundocode.pomodoro.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.kiwi.navigationcompose.typed.composable
import com.kiwi.navigationcompose.typed.createRoutePattern
import com.mundocode.pomodoro.ui.screens.habits.HabitsScreen
import com.mundocode.pomodoro.ui.screens.homeScreen.HomeScreen
import com.mundocode.pomodoro.ui.screens.loginScreen.LoginScreen
import com.mundocode.pomodoro.ui.screens.loginScreen.RegisterScreen
import com.mundocode.pomodoro.ui.screens.points.StoreScreen
import com.mundocode.pomodoro.ui.screens.setupSessionScreen.SetupSessionScreen
import com.mundocode.pomodoro.ui.screens.splashScreen.SplashScreen
import com.mundocode.pomodoro.ui.screens.taskScreen.TaskScreen
import com.mundocode.pomodoro.ui.screens.timer.TimerScreen
import kotlinx.serialization.ExperimentalSerializationApi
import com.mundocode.pomodoro.core.navigation.Destinations.Task

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun NavigationRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = createRoutePattern<Destinations.Splash>(),
    ) {
        composable<Destinations.Splash> {
            SplashScreen(
                navController = navController,
            )
        }

        composable<Destinations.Login> {
            LoginScreen(
                navController = navController,
            )
        }
        composable<Destinations.Register> {
            RegisterScreen(
                navController = navController,
            )
        }
        composable<Destinations.Home> {
            HomeScreen(navController = navController)
        }
        composable<Destinations.SetupSession> {
            SetupSessionScreen(
                navController = navController,
            )
        }
        composable<Destinations.Habits> {
            HabitsScreen(navController = navController)
        }

        composable<Task> {
            TaskScreen(navController = navController)
        }

        composable<Destinations.Timer> {
            TimerScreen(navController = navController)
        }
        composable<Destinations.Store> {
            StoreScreen(navController = navController)
        }
    }
}
